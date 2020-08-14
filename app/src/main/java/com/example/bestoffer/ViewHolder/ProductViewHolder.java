package com.example.bestoffer.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestoffer.Interfaces.ItemClickLisener;
import com.example.bestoffer.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtProductname,txtProductDescription,txtProductPrice;
    public ImageView imageView;
    public ItemClickLisener lisener;


    public ProductViewHolder(View itemView) {
        super(itemView);
        imageView =(ImageView)itemView.findViewById(R.id.productImageIv);
        txtProductname =(TextView) itemView.findViewById(R.id.name);
        txtProductDescription =(TextView)itemView.findViewById(R.id.descriptionTv);
        txtProductPrice =(TextView)itemView.findViewById(R.id.priceTv);
    }

    public TextView getTxtProductname() {
        return txtProductname;
    }

    public void setTxtProductname(TextView txtProductname) {
        this.txtProductname = txtProductname;
    }

    public TextView getTxtProductDescription() {
        return txtProductDescription;
    }

    public void setTxtProductDescription(TextView txtProductDescription) {
        this.txtProductDescription = txtProductDescription;
    }

    public TextView getTxtProductPrice() {
        return txtProductPrice;
    }

    public void setTxtProductPrice(TextView txtProductPrice) {
        this.txtProductPrice = txtProductPrice;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onClick(View view) {

        lisener.OnClick(view,getAdapterPosition(),false);

    }


    public void setitemclicklisiner(ItemClickLisener itemClickLisener)
    {
        itemClickLisener= lisener;

    }
}
