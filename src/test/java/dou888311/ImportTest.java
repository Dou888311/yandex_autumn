package dou888311;

import dou888311.dto.SystemItemHistoryResponse;
import dou888311.dto.SystemItemImport;
import dou888311.dto.SystemItemImportRequest;
import dou888311.dto.SystemItemType;
import dou888311.entity.SystemItem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ImportTest {

    private final String hostImport = "http://127.0.0.1:80/imports";
    private final String yandexHost = "https://zones-1883.usr.yandex-academy.ru/imports";
    private List<SystemItemImportRequest> batch = new ArrayList<>();

    {
        var rootFolder = new SystemItemImport.SystemItemImportBuilder()
                .id("rootFolder")
                .type(SystemItemType.FOLDER)
                .parentId(null)
                .build();
        SystemItemImportRequest rootRequest = new SystemItemImportRequest(List.of(rootFolder), LocalDateTime.of(2022, 10, 10, 10, 10, 0));
        batch.add(rootRequest);

        var folder1 = new SystemItemImport.SystemItemImportBuilder()
                .id("folder1")
                .type(SystemItemType.FOLDER)
                .parentId("rootFolder")
                .build();
        var file1 = new SystemItemImport.SystemItemImportBuilder()
                .id("file1")
                .type(SystemItemType.FILE)
                .parentId("folder1")
                .size(333)
                .url("folder1/file1")
                .build();
        var file2 = new SystemItemImport.SystemItemImportBuilder()
                .id("file2")
                .type(SystemItemType.FILE)
                .parentId("folder1")
                .size(124)
                .url("folder1/file2")
                .build();
        var folder2 = new SystemItemImport.SystemItemImportBuilder()
                .id("folder2")
                .type(SystemItemType.FOLDER)
                .parentId("folder1")
                .build();
        var file3 = new SystemItemImport.SystemItemImportBuilder()
                .id("file3")
                .type(SystemItemType.FILE)
                .parentId("folder2")
                .size(256)
                .url("folder1/folder2/file3")
                .build();
        SystemItemImportRequest request2 = new SystemItemImportRequest(List.of(folder1, file1, file2, folder2, file3),
                LocalDateTime.of(2022,10,10,15,10,0));
        batch.add(request2);

        var file4 = new SystemItemImport.SystemItemImportBuilder()
                .id("file4")
                .type(SystemItemType.FILE)
                .parentId("rootFolder")
                .url("/file4")
                .size(512)
                .build();
        var file5 = new SystemItemImport.SystemItemImportBuilder()
                .id("file5")
                .type(SystemItemType.FILE)
                .parentId("rootFolder")
                .url("/file5")
                .size(1024)
                .build();
        SystemItemImportRequest request3 = new SystemItemImportRequest(List.of(file4, file5),
                LocalDateTime.of(2022,10,11,10,10,0));
        batch.add(request3);

        RestTemplate restTemplate = new RestTemplate();
        for (var item : batch) {
            HttpEntity<SystemItemImportRequest> entity = new HttpEntity<>(item);
            restTemplate.postForEntity(yandexHost, entity, SystemItemImportRequest.class);
        }
    }

    /**
     * Schema after import from instance initialization block:
     *   rootFolder:
     *      folder1:
     *          file1 (size 333)
     *          file2 (size 124)
     *          folder2:
     *              file3 (size 256)
     *      file4 (size 512)
     *      file5 (size 1024)
     */

    @Test
    public void simpleImportChecking() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SystemItem> response = restTemplate.getForEntity("https://zones-1883.usr.yandex-academy.ru/nodes/folder2", SystemItem.class);
        Assertions.assertEquals(256, response.getBody().getSize());
    }

    @Test
    public void fileCantBeParentOfAnotherFileShouldThrowBadRequest() {
        var restTemplate = new RestTemplate();
        var file3TryingUpdate = new SystemItemImport.SystemItemImportBuilder()
                .id("file3")
                .parentId("file2")
                .build();
        var request = new SystemItemImportRequest(List.of(file3TryingUpdate), LocalDateTime.of(2022,10,10,10, 15,0));
        HttpEntity<SystemItemImportRequest> entity = new HttpEntity<>(request);
        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            ResponseEntity<SystemItemImportRequest> response = restTemplate.postForEntity(yandexHost, entity, SystemItemImportRequest.class);
        });
    }

    @Test
    public void simpleGetNodesShould200() {
        var restTemplate = new RestTemplate();
        var response = restTemplate.getForEntity("https://zones-1883.usr.yandex-academy.ru/nodes/rootFolder", SystemItem.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void invalidIdShould404() {
        var restTemplate = new RestTemplate();
        Assertions.assertThrows(HttpClientErrorException.NotFound.class, () -> {
            var response = restTemplate.getForEntity("https://zones-1883.usr.yandex-academy.ru/nodes/invalidId123456", SystemItem.class);
            Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        });
    }

    @Test
    public void getValidFolderWithCorrectSize() {
        var restTemplate = new RestTemplate();
        var response = restTemplate.getForEntity("https://zones-1883.usr.yandex-academy.ru/nodes/folder2", SystemItem.class);
        Assertions.assertEquals(256, response.getBody().getSize());

        response = restTemplate.getForEntity("https://zones-1883.usr.yandex-academy.ru/nodes/folder1", SystemItem.class);
        Assertions.assertEquals(713, response.getBody().getSize());

        response = restTemplate.getForEntity("https://zones-1883.usr.yandex-academy.ru/nodes/rootFolder", SystemItem.class);
        Assertions.assertEquals(2249, response.getBody().getSize());
    }

    @Test
    public void folderHaveValidChildrenSize() {
        var restTemplate = new RestTemplate();
        var response = restTemplate.getForEntity("https://zones-1883.usr.yandex-academy.ru/nodes/folder1", SystemItem.class);
        Assertions.assertEquals(3, response.getBody().getChildren().size());
    }

    /**
     * batch1(rootFolder) 10.10.22 10:10:00
     * batch2(folder1, file1, file2, folder2, file3) 10.10.22 15:10:00
     * batch3(file4, file5) 11.10.22 10:10:00
     */

    @Test
    public void updateForLast24HoursAndContainsOnlyFiles() {
        String date = "2022-10-10T15:10:01.000Z";
        var restTemplate = new RestTemplate();
        var response = restTemplate.getForEntity("https://zones-1883.usr.yandex-academy.ru/updates?date=" + date,
                SystemItemHistoryResponse.class);
        Assertions.assertEquals(3, response.getBody().getItems().size());
        Assertions.assertTrue(() -> {
           for (var item : response.getBody().getItems()) {
               if (item.getType() == SystemItemType.FOLDER) {
                   return false;
               }
           }
           return true;
        });
    }

    @Test
    public void deletingChildFolderShouldUpdateDateForParent() {
        var restTemplate = new RestTemplate();
        restTemplate.delete("https://zones-1883.usr.yandex-academy.ru/delete/file2?date=2023-12-12T15:10:00Z");
        var parentWithDate = restTemplate.getForEntity("https://zones-1883.usr.yandex-academy.ru/nodes/folder1", SystemItem.class);
        Assertions.assertEquals(LocalDateTime.ofInstant(Instant.parse("2023-12-12T15:10:00Z"), ZoneOffset.UTC),
                parentWithDate.getBody().getDate());
    }

    @Test
    public void cantGetHistoryForDeletingUnit() {
        var restTemplate = new RestTemplate();
        restTemplate.delete("https://zones-1883.usr.yandex-academy.ru/delete/file4?date=2023-12-12T15:10:00Z");
        Assertions.assertThrows(HttpClientErrorException.NotFound.class, () -> {
            restTemplate.getForEntity("https://zones-1883.usr.yandex-academy.ru/nodes/file4", SystemItem.class);
        });
    }

    @AfterAll
    public static void deleteRoot() {
        new RestTemplate().delete("https://zones-1883.usr.yandex-academy.ru/delete/rootFolder?date=2023-12-12T16:00:00Z");
    }
}

