// Generated by view binder compiler. Do not edit!
package com.example.bestoffer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import com.example.bestoffer.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FavoritesItemLayoutBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final ImageView deleteIv;

  @NonNull
  public final TextView priceLabelTv;

  @NonNull
  public final TextView priceTv;

  @NonNull
  public final TextView productNameTv;

  @NonNull
  public final TextView theSellerLabelTv;

  @NonNull
  public final TextView theSellerTv;

  private FavoritesItemLayoutBinding(@NonNull CardView rootView, @NonNull ImageView deleteIv,
      @NonNull TextView priceLabelTv, @NonNull TextView priceTv, @NonNull TextView productNameTv,
      @NonNull TextView theSellerLabelTv, @NonNull TextView theSellerTv) {
    this.rootView = rootView;
    this.deleteIv = deleteIv;
    this.priceLabelTv = priceLabelTv;
    this.priceTv = priceTv;
    this.productNameTv = productNameTv;
    this.theSellerLabelTv = theSellerLabelTv;
    this.theSellerTv = theSellerTv;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static FavoritesItemLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FavoritesItemLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.favorites_item_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FavoritesItemLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.deleteIv;
      ImageView deleteIv = rootView.findViewById(id);
      if (deleteIv == null) {
        break missingId;
      }

      id = R.id.priceLabelTv;
      TextView priceLabelTv = rootView.findViewById(id);
      if (priceLabelTv == null) {
        break missingId;
      }

      id = R.id.priceTv;
      TextView priceTv = rootView.findViewById(id);
      if (priceTv == null) {
        break missingId;
      }

      id = R.id.productNameTv;
      TextView productNameTv = rootView.findViewById(id);
      if (productNameTv == null) {
        break missingId;
      }

      id = R.id.theSellerLabelTv;
      TextView theSellerLabelTv = rootView.findViewById(id);
      if (theSellerLabelTv == null) {
        break missingId;
      }

      id = R.id.theSellerTv;
      TextView theSellerTv = rootView.findViewById(id);
      if (theSellerTv == null) {
        break missingId;
      }

      return new FavoritesItemLayoutBinding((CardView) rootView, deleteIv, priceLabelTv, priceTv,
          productNameTv, theSellerLabelTv, theSellerTv);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
