package com.oysterworld.portfolio.owproject_backend.app.business.blog;

import com.oysterworld.portfolio.owproject_backend.common.IdHashUtils;
import com.oysterworld.portfolio.owproject_backend.exception.OwBadRequestException;
import com.oysterworld.portfolio.owproject_backend.exception.OwInternalServerErrorException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest(classes = com.oysterworld.portfolio.owproject_backend.app.OwprojectBackendApplication.class)
@Transactional
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.oysterworld.portfolio.owproject_backend")
public class BlogBusinessLogicTest {
    @Autowired
    public BlogBusinessLogic blogBusinessLogic;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testGetContentByTitleNoResult() {
        var result = blogBusinessLogic.getContentByTitle("title");
        assertEquals(0, result.size());
    }

    @Test
    void testGetContentByTitleHasManyResult() {
        addTestData(3);
        var result = blogBusinessLogic.getContentByTitle("title");
        assertEquals(3, result.size());
        var tmpId = new AtomicReference<>("");
        IntStream.range(0, 3).forEach((n) -> {
            var number = n + 1;
            var id = result.get(n).getId();
            assertTrue(id.matches("^[a-f0-9]{64}$"));
            assertNotEquals(id, tmpId.get());
            assertEquals("test-title-" + number, result.get(n).getTitle());
            assertEquals("test-content-" + number, result.get(n).getContent());
            assertEquals(String.valueOf(number), result.get(n).getInnerId());
            tmpId.set(id);
        });
    }

    @Test
    void testGetContentByTitleErrorTitleBlank() {
        try {
            blogBusinessLogic.getContentByTitle("");
            fail();
        } catch (OwBadRequestException e) {
            assertEquals("input parameter is empty. Input Value : \"\"", e.getMessage());
        }
    }

    @Test
    void testGetContentByTitleErrorTitleAllSpace() {
        try {
            blogBusinessLogic.getContentByTitle("        ");
            fail();
        } catch (OwBadRequestException e) {
            assertEquals("input parameter is empty. Input Value : \"        \"", e.getMessage());
        }
    }

    @Test
    void testGetContentByIdNoResult() {
        var result = blogBusinessLogic.getContentById("id");
        assertNull(result.getId());
        assertNull(result.getTitle());
        assertNull(result.getContent());
        assertNull(result.getInnerId());
    }

    @Test
    void testGetContentByIdHasResult() {
        addTestData(3);
        var result = blogBusinessLogic.getContentById("UNKNOWN");
        assertNull(result.getId());
        assertNull(result.getTitle());
        assertNull(result.getContent());
        assertNull(result.getInnerId());
        var id = blogBusinessLogic.getAllContents().get(0).getId();
        result = blogBusinessLogic.getContentById(id);
        assertTrue(result.getId().matches("^[a-f0-9]{64}$"));
        assertEquals("test-title-1", result.getTitle());
        assertEquals("test-content-1", result.getContent());
        assertEquals(String.valueOf(1), result.getInnerId());
    }

    @Test
    void testGetContentByIdErrorTitleBlank() {
        try {
            blogBusinessLogic.getContentById("");
            fail();
        } catch (OwBadRequestException e) {
            assertEquals("input parameter is empty. Input Value : \"\"", e.getMessage());
        }
    }

    @Test
    void testGetContentByIdErrorTitleAllSpace() {
        try {
            blogBusinessLogic.getContentById("        ");
            fail();
        } catch (OwBadRequestException e) {
            assertEquals("input parameter is empty. Input Value : \"        \"", e.getMessage());
        }
    }

    @Test
    void testGetAllContentHasNoResult() {
        var result = blogBusinessLogic.getAllContents();
        assertEquals(0, result.size());
    }

    @Test
    void testGetAllContentHasManyResult() {
        addTestData(10);
        var result = blogBusinessLogic.getAllContents();
        assertEquals(10, result.size());
        var tmpId = new AtomicReference<>("");
        IntStream.range(0, 10).forEach((n) -> {
            var number = n + 1;
            var id = result.get(n).getId();
            assertTrue(id.matches("^[a-f0-9]{64}$"));
            assertNotEquals(id, tmpId.get());
            assertEquals("test-title-" + number, result.get(n).getTitle());
            assertEquals("test-content-" + number, result.get(n).getContent());
            assertEquals(String.valueOf(number), result.get(n).getInnerId());
            tmpId.set(id);
        });
    }

    @Test
    void testPostContentHasOneResult() {
        var blog = new Blog(null, "test-title", "test-content", null);

        var result = blogBusinessLogic.postContent(blog);
        assertTrue(result.getId().matches("^[a-f0-9]{64}$"));
        assertEquals("test-title", result.getTitle());
        assertEquals("test-content", result.getContent());
        assertEquals("1", result.getInnerId());

        var dbResult = jdbcTemplate.queryForList("SELECT * FROM BLOG");
        assertEquals(1, dbResult.size());
        assertEquals(result.getId(), dbResult.get(0).get("id"));
        assertEquals("test-title", dbResult.get(0).get("title"));
        assertEquals("test-content", dbResult.get(0).get("content"));
        assertEquals(1, dbResult.get(0).get("inner_id"));
    }

    @Test
    void testPostContentHasManyResult() {
        var blog1 = new Blog(null, "test-title-1", "test-content", null);
        var blog2 = new Blog(null, "test-title-2", "test-content", null);
        blogBusinessLogic.postContent(blog1);
        blogBusinessLogic.postContent(blog2);
        var dbResult = jdbcTemplate.queryForList("SELECT * FROM BLOG");
        assertEquals(2, dbResult.size());
        assertEquals(1, dbResult.get(0).get("inner_id"));
        assertEquals(2, dbResult.get(1).get("inner_id"));
    }

    @Test
    void testPostContentErrorTitleAlreadyExists() {
        var blog1 = new Blog(null, "test-title", "test-content-1", null);
        var blog2 = new Blog(null, "test-title", "test-content-2", null);
        try {
            blogBusinessLogic.postContent(blog1);
            blogBusinessLogic.postContent(blog2);
            fail();
        } catch (OwBadRequestException e) {
            assertEquals("input title already exists. Input Value : \"test-title\"", e.getMessage());
        }
    }

    @Test
    void testPostContentErrorDuplicateId() {
        try (MockedStatic<IdHashUtils> mockedStatic = mockStatic(IdHashUtils.class)) {
            String fixedHashedId = "test-hash-id";
            mockedStatic.when(() -> IdHashUtils.hashId(anyString())).thenReturn(fixedHashedId);
            var blog1 = new Blog(null, "test-title-1", "test-content-1", null);
            var blog2 = new Blog(null, "test-title-2", "test-content-2", null);
            blogBusinessLogic.postContent(blog1);
            blogBusinessLogic.postContent(blog2);
            fail();
        } catch (OwInternalServerErrorException e) {
            assertEquals("Duplicate id detected.", e.getMessage());
        }
    }

    private void addTestData(int count) {
        IntStream.range(0, count).forEach((n) -> {
            var blog = new Blog("test-id-" + (n + 1), "test-title-" + (n + 1), "test-content-" + (n + 1),
                    String.valueOf(n + 1));
            blogBusinessLogic.postContent(blog);
        });
    }
}