package com.unimib.triviaducks.adapter;

import static com.unimib.triviaducks.util.Constants.CODE_GEOGRAPHY;
import static com.unimib.triviaducks.util.Constants.CODE_HISTORY;
import static com.unimib.triviaducks.util.Constants.CODE_SCIENCE_NATURE;
import static com.unimib.triviaducks.util.Constants.CODE_SPORTS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.util.Constants;

import java.util.ArrayList;

public class CategoriesPodiumAdapter extends ArrayAdapter<String> {

    private int layout;
    private ArrayList<String> categoriesList;

    public CategoriesPodiumAdapter(@NonNull Context context, int layout, @NonNull ArrayList<String> categoriesList) {
        super(context, layout, categoriesList);
        this.layout = layout;
        this.categoriesList = categoriesList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);

        int positionCategoryList = getCategoryFromCode(Integer.parseInt(categoriesList.get(position)));

        ImageView categoryPosition = convertView.findViewById(R.id.categoryPosition);
        LottieAnimationView categoryImage= convertView.findViewById(R.id.categoryImage);
        TextView categoryTextView = convertView.findViewById(R.id.categoryTextView);

        categoryPosition.setImageDrawable(ContextCompat.getDrawable(getContext(), getImagePodium(position)));
        categoryImage.setAnimation(Constants.LIST_LOTTIE_ANIMATIONS.get(positionCategoryList));
        categoryTextView.setText(Constants.LIST_CATEGORY.get(positionCategoryList));

        return convertView;
    }

    private int getCategoryFromCode(int code) {
        switch (code) {
            case CODE_HISTORY:
                return 3;
            case CODE_SCIENCE_NATURE:
                return 1;
            case CODE_GEOGRAPHY:
                return 2;
            case CODE_SPORTS:
                return 4;
            default:
                return 0;
        }
    }

    private int getImagePodium(int position) {
        switch (position) {
            case 1:
                return R.drawable.medal2;
            case 2:
                return R.drawable.medal3;
            default:
                return R.drawable.medal1;
        }
    }
}
