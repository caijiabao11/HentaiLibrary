package com.example.administrator.lztsg;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.ItemHolder> {
    private Context mcontext;
    private List<Item> mItems;
    private ArrayList<Item> filterWords;
    private List<Item> mCopyInviteMessages;

    LinearAdapter(List<Item> items) {
        mItems = items;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        //设置Item图片
        holder.image.setImageResource(mItems.get(position).getImageResId());
        //设置Item文字
        holder.title.setText(mItems.get(position).getTitle());

       // holder.title.setText(mFilterList.get(position));
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.layout_linear_item, parent, false));
    }
    public Filter getFilter(){
        return new Filter() {

            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //String charString = constraint.toString();
                FilterResults results  = new FilterResults();
                if(results.values == null){
                    //没有过滤的内容，则使用源数据
                    mItems.clear();
                    mItems.addAll(mCopyInviteMessages);
                }
                //关键字为空的时候，搜索结果为复制的结果
                if (constraint == null||constraint.length() == 0){
                    results.values = mCopyInviteMessages;
                    results.count = mCopyInviteMessages.size();
                }else{
                    String prefixString = constraint.toString();
                    final int count = mItems.size();
                    //用于存放暂时的过滤结果
                    final ArrayList<Item> newValues = new ArrayList<Item>();
                    for (int i=0;i<count;i++){
                        final Item value = mItems.get(i);
                        String username= value.getTitle();
                        if (username.contains(prefixString)){
                            newValues.add(value);
                        }else{
                            final String[] words = username.split("");
                            final int wordCount = words.length;

                            for (int k=0;k<wordCount;k++){
                                if (words[k].contains(prefixString)){
                                    newValues.add(value);
                                    break;
                                }
                            }
                        }
                    }
                    results.values = newValues;
                    results.count = newValues.size();
                }
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mItems.clear();//清除数据
                mItems.addAll((List<Item>) results.values);//将过滤结果添加到这个对象
                if (results.count == 0){
                    //有关键字的时候刷新数据
                    notifyDataSetChanged();
                }else{
                    //关键字不为零但过滤结果为空刷新数据
                    if (constraint.length() !=0){
                        notifyDataSetChanged();
                        return;
                    }
                    setFilter((ArrayList<Item>) mCopyInviteMessages);
                }
            }
        };
    }

    public void setFilter(ArrayList<Item> filterWords) {
        mCopyInviteMessages = new ArrayList<>();
        this.mCopyInviteMessages.addAll(filterWords);
        this.mItems=filterWords;
        this.notifyDataSetChanged();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;

        ItemHolder(View item) {
            super(item);
            image = item.findViewById(R.id.image_view);
            title = item.findViewById(R.id.text_title);
        }
    }
}
