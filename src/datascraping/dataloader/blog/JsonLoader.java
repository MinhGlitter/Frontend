package datascraping.dataloader.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import datascraping.model.Blog;
import datascraping.model.BlogEntity;
import datascraping.model.CollectionEntity;
import org.json.JSONArray;

import java.io.IOException;
import java.util.*;

public abstract class JsonLoader extends FileLoader {

    public JsonLoader(String source) {
        super(source);
    }

    @Override
    public Collection<BlogEntity> load() {
        Collection<BlogEntity> blogEntities = new ArrayList<>();

        try {
            List<Map<String, Object>> dataList = new ObjectMapper().readValue(file, ArrayList.class);
            for (Map<String, Object> data : dataList) {
                String link = getStringAndRemove(data, "link");
                String img = getStringAndRemove(data, "img");
                String title = getStringAndRemove(data, "title");
                String author = getStringAndRemove(data, "author");
                String content = getStringAndRemove(data, "content");
                String date = getStringAndRemove(data, "content");
                List<String> tag = getListAndRemove(data, "tag");

                BlogEntity entity = createSpecificEntity(
                        link, img, title, author, content, date, tag
                );

                blogEntities.add(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return blogEntities;
    }

    // Abstract method for creating specific entities, to be implemented by subclasses
    protected abstract BlogEntity createSpecificEntity(
            String link, String img, String title, String content, String author, String date, List<String> tag
    );

    protected String getStringAndRemove(Map<String, Object> data, String key) {
        String value = (String) data.get(key);
        data.remove(key);
        return value;
    }

    protected List<String> getListAndRemove(Map<String, Object> data, String key) {
        String value = data.get(key).toString();
        List<String> tags = Arrays.asList(value.substring(1, value.length() - 1).split(", "));
        data.remove(key);
        return tags;
    }
}