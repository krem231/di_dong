package com.example.manga;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Overview extends Fragment {

    private TextView overviewText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview, container, false);
        overviewText = view.findViewById(R.id.overview_text);

        // Cái này là hàm để lấy thông tin của manga, mỗi manga thì có 1 id nên nhớ để ý, cái id đó thì nó nằm lúc thêm data vào ở main activity
        Bundle args = getArguments();
        if (args != null) {
            String mangaId = args.getString("mangaId");
            MangaDataProvider.Manga manga = MangaDataProvider.getMangaById(mangaId);
            if (manga != null) {
                overviewText.setText(manga.overview);
            }
        }
        return view;
    }
}
