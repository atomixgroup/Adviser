package ir.codetower.moshaver.Adapters;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ir.codetower.moshaver.Activities.ContentActivity;
import ir.codetower.moshaver.App;
import ir.codetower.moshaver.CustomTextView;
import ir.codetower.moshaver.Models.Category;
import ir.codetower.moshaver.Models.Content;
import ir.codetower.moshaver.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ProductListViewHolder> implements Filterable {
    private ArrayList<Content> products;
    private ArrayList<Content> mFilteredList;

    public SearchAdapter(ArrayList<Content> arrayList) {
        products = arrayList;
        mFilteredList = arrayList;

        for (int i=0;i<products.size();i++ ) {
            Category cat=Category.getCAtegory(products.get(i).getG_id());
            Content content=products.get(i);
            content.setCategory_title(cat.getTitle());
            products.set(i,content);
        }

    }

    @Override
    public SearchAdapter.ProductListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_adapter, viewGroup, false);
        return new SearchAdapter.ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductListViewHolder viewHolder, int i) {
        viewHolder.bind(mFilteredList.get(i),i);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = products;
                } else {

                    ArrayList<Content> filteredList = new ArrayList<>();

                    for (Content product : products) {

                        if (product.getTitle().toLowerCase().contains(charString) || product.getBody().toLowerCase().contains(charString) ) {

                            filteredList.add(product);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Content>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ProductListViewHolder extends RecyclerView.ViewHolder{

        private CustomTextView item_title;
        private CustomTextView item_category;
        private CustomTextView item_price;
        private CardView item_root;

        public ProductListViewHolder(View itemView) {
            super(itemView);


            item_category= itemView.findViewById(R.id.item_category);
            item_category.setVisibility(View.VISIBLE);
            item_title= itemView.findViewById(R.id.item_title);
            item_root=  itemView.findViewById(R.id.item_root);
            item_price=itemView.findViewById(R.id.item_price);
            item_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(App.context, ContentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id",mFilteredList.get(getLayoutPosition()).getId());
                    App.context.startActivity(intent);
                }
            });


        }

        public void bind(final Content content, final int position) {

            item_title.setText(content.getTitle());
            item_category.setText(content.getCategory_title());
            if (content.getPrice().equals("0")){
                item_price.setText("رایگان");
            }
            else{
                item_price.setText(content.getPrice()+" تومان");
            }

        }

    }

}