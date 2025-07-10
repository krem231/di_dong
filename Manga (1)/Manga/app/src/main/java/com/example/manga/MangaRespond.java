package com.example.manga;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
public class MangaRespond {
    // Lưu danh sách dữ liệu được parse từ JSON
    public List<MangaData> data;
    public List<MangaData> getData() { return data; }
    // Trả danh sách Manga theo định dạng dùng trong app
    public List<MangaDataProvider.Manga> toMangaList() {
        List<MangaDataProvider.Manga> result = new ArrayList<>();
        if (data != null) {
            for (MangaData item : data) {
                String title = item.attributes.title.get("en");
                String overview = item.attributes.overview.get("en");

                // Lấy fileName từ relationship cover_art
                String fileName = null;
                if (item.relationships != null) {
                    for (Relationship rel : item.relationships) {
                        if ("cover_art".equals(rel.type) && rel.attributes != null) {
                            fileName = rel.attributes.fileName;
                            break;
                        }
                    }
                }

                String imageUrl = null;
                if (fileName != null) {
                    imageUrl = "https://uploads.mangadex.org/covers/" + item.id + "/" + fileName + ".512.jpg";
                }

                result.add(new MangaDataProvider.Manga(
                        item.id,
                        title != null ? title : "No Title",
                        overview != null ? overview : "No Overview",
                        imageUrl
                ));
            }
        }
        return result;
    }
}
