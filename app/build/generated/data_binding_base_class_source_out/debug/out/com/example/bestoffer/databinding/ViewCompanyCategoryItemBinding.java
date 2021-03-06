// Generated by view binder compiler. Do not edit!
package com.example.bestoffer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.example.bestoffer.R;
import com.google.android.material.card.MaterialCardView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ViewCompanyCategoryItemBinding implements ViewBinding {
  @NonNull
  private final MaterialCardView rootView;

  @NonNull
  public final ImageView addProductUnderCategory;

  @NonNull
  public final ImageView arrowIv;

  @NonNull
  public final TextView categoryNameTv;

  @NonNull
  public final RecyclerView productsRv;

  private ViewCompanyCategoryItemBinding(@NonNull MaterialCardView rootView,
      @NonNull ImageView addProductUnderCategory, @NonNull ImageView arrowIv,
      @NonNull TextView categoryNameTv, @NonNull RecyclerView productsRv) {
    this.rootView = rootView;
    this.addProductUnderCategory = addProductUnderCategory;
    this.arrowIv = arrowIv;
    this.categoryNameTv = categoryNameTv;
    this.productsRv = productsRv;
  }

  @Override
  @NonNull
  public MaterialCardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ViewCompanyCategoryItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ViewCompanyCategoryItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.view_company_category_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ViewCompanyCategoryItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.addProductUnderCategory;
      ImageView addProductUnderCategory = rootView.findViewById(id);
      if (addProductUnderCategory == null) {
        break missingId;
      }

      id = R.id.arrowIv;
      ImageView arrowIv = rootView.findViewById(id);
      if (arrowIv == null) {
        break missingId;
      }

      id = R.id.categoryNameTv;
      TextView categoryNameTv = rootView.findViewById(id);
      if (categoryNameTv == null) {
        break missingId;
      }

      id = R.id.productsRv;
      RecyclerView productsRv = rootView.findViewById(id);
      if (productsRv == null) {
        break missingId;
      }

      return new ViewCompanyCategoryItemBinding((MaterialCardView) rootView,
          addProductUnderCategory, arrowIv, categoryNameTv, productsRv);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
