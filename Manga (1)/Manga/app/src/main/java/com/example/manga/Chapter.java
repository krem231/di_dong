package com.example.manga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Chapter extends Fragment {

    private ListView chapterListView;

    private List<ChapterModel> chapterModels = new ArrayList<>();
    private List<String> chapterIdList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);
        chapterListView = view.findViewById(R.id.chapterList);

        String mangaId = getArguments() != null ? getArguments().getString("mangaId") : null;

        if (mangaId != null) {
            MangaChapterAPI.fetchChapters(mangaId, new MangaCallback<List<ChapterData>>() {
                @Override
                public void onSuccess(List<ChapterData> chapterDataList) {
                    List<ChapterModel> chapters = MangaChapterAPI.convertToModels(chapterDataList, mangaId);
                    chapterModels.clear();
                    chapterModels.addAll(chapters);

                    List<String> chapterTitles = new ArrayList<>();
                    for (ChapterModel model : chapters) {
                        String title = model.getTitle() != null ? model.getTitle() : "Chương " + model.getNumber();
                        chapterTitles.add(title);
                    }

                    requireActivity().runOnUiThread(() -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, chapterTitles) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView text = view.findViewById(android.R.id.text1);
                                text.setTextColor(getResources().getColor(android.R.color.white)); // ✅ ép màu trắng
                                return view;
                            }
                        };

                        chapterListView.setAdapter(adapter);
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show()
                    );
                }
            });
        }


        chapterListView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position < chapterModels.size()) {
                ChapterModel selected = chapterModels.get(position);

                Intent intent = new Intent(getActivity(), ChapterDetailActivity.class);
                intent.putExtra("chapter_id", selected.getChapterId());
                intent.putExtra("chapter_number", selected.getNumber());
                intent.putExtra("manga_id", selected.getMangaId());
                startActivity(intent);
            }
        });


        return view;
    }
}
