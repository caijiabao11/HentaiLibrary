package com.example.administrator.lztsg.activity;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.MyApplication;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.items.AsmrItem;
import com.example.administrator.lztsg.items.CbirSitesItem;
import com.example.administrator.lztsg.items.Hpoidetailpageitem;
import com.example.administrator.lztsg.items.Hpoiitem;
import com.example.administrator.lztsg.items.Item;
import com.example.administrator.lztsg.items.MoreItem;
import com.example.administrator.lztsg.items.MultipleItem;
import com.example.administrator.lztsg.items.TestholeItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LinearAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mcontext;
    private List<MultipleItem> mItems;
    private ArrayList<MultipleItem> filterWords;
    private List<MultipleItem> mCopyInviteMessages;
    private OnItemClickListener mListener;
    private boolean mQieHuan;
    /**
     * 切换布局 true为一列 false为两列
     */
    private static final int ITEMZERO = 0;
    private static final int ITEMONE = 1;
    private static final int ITEMTWO = 2;
    private static final int ITEMTHREE = 3;
    private static final int ITEMFUOR = 4;
    private static final int ITEMFIVE = 5;
    private static final int ITEMSIX = 6;

    LinearAdapter(List<MultipleItem> items, OnItemClickListener listener) {
        mItems = items;
        mListener = listener;

    }
    public LinearAdapter(Context context, List<MultipleItem> items, OnItemClickListener listener) {
        mcontext = context;
        mItems = items;
        mListener = listener;

    }

    LinearAdapter(List<MultipleItem> items, boolean qieHuan, OnItemClickListener listener) {
        mItems = items;
        mQieHuan = qieHuan;
        mListener = listener;

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        //根据某一个参数不同的值来返回对应的Type
        switch (mItems.get(position).getItemType()) {
            case HENTAI:
                return ITEMZERO;
            case MORE:
                return ITEMONE;
            case ASMR:
                return ITEMTWO;
            case TESTHOLE:
                return ITEMTHREE;
            case SEARCH_IMG:
                return ITEMFUOR;
            case HPOI:
                return ITEMFIVE;
            case HPOIDETAILPAGE:
                return ITEMSIX;
        }
        return -1;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        addAnimation(holder);
    }

    private void addAnimation(RecyclerView.ViewHolder holder) {
        for (Animator anim : getAnimators(holder.itemView)) {
            anim.setDuration(300).start();
        }
    }

    private Animator[] getAnimators(View view) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);
        return new ObjectAnimator[]{alpha};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        switch (mItems.get(position).getItemType()) {
            case HENTAI:
                final Item item = (Item) mItems.get(position);
                final ItemHolder itemHolder = (ItemHolder) holder;
                //设置Item图片
                Glide.with(MyApplication.getContext())
                        .load(item.getImgurl())
                        .placeholder(R.drawable.none)
                        .fitCenter()
                        .into(itemHolder.image);
                //设置Item文字
                itemHolder.title.setText(item.getTitle());
//                itemHolder.itemView.setAnimation(AnimationUtils.loadAnimation(itemHolder.itemView.getContext(),R.anim.alpha));
                //设置点击事件
                itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.itemonClick(position, mItems);
//                        mListener.itemHoldersonClick(position);
                    }
                });
                break;
            case MORE:
                MoreItem item1 = (MoreItem) mItems.get(position);
                MoreHolder moreHolder = (MoreHolder) holder;
                //设置Item图片
                moreHolder.image.setImageResource(item1.getImageResId());
                //设置Item文字
                moreHolder.title.setText(item1.getTitle());
                moreHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.itemHoldersonClick(position);
                    }
                });
                break;
            case ASMR:
                AsmrItem item2 = (AsmrItem) mItems.get(position);
                AsmrHolder asmrHolder = (AsmrHolder) holder;
                //设置Item图片
                Glide.with(MyApplication.getContext())
                        .load(item2.getCoverUrl())
                        .fitCenter()
                        .into(asmrHolder.image);
                //设置Item文字
                asmrHolder.title.setText(item2.getTitle());
                //设置Item编号
                asmrHolder.id.setText("RJ0"+ item2.getId());
                //设置Item日期
                asmrHolder.release.setText(item2.getRelease());
                //设置Item标签
                addTag(asmrHolder.tags,item2.getTags());

                asmrHolder.name.setText(item2.getName());
                asmrHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.itemonClick(position,mItems);
                    }
                });
                break;
            case TESTHOLE:
                TestholeItem item3 = (TestholeItem) mItems.get(position);
                TestholeHolder testholeHolder = (TestholeHolder) holder;
                //设置Item图片
//                testholeHolder.image.setImageURI(Uri.parse(item3.getmImageUrl()));
                //Glide设置图片圆角角度
//                RoundedCorners roundedCorners = new RoundedCorners(50);
                //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//                 RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(50));
//                 RequestOptions options1 = new RequestOptions()
//                         .skipMemoryCache(true)
//                         .diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(MyApplication.getContext())
                        .load(item3.getmImageUrl())
                        .fitCenter()
                        .into(testholeHolder.image);
                //设置Item文字
                testholeHolder.title.setText(item3.getTitle());
                //设置Item持续时间
                testholeHolder.duration.setText(item3.getmDuration());
                testholeHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.itemonClick(position, mItems);
                    }
                });
                break;
            case SEARCH_IMG:
                CbirSitesItem item4 = (CbirSitesItem) mItems.get(position);
                CbirSitesHolder cbirSitesHolder = (CbirSitesHolder) holder;
                //设置Item图片
                Glide.with(MyApplication.getContext())
                        .load(item4.getImageUrl())
                        .fitCenter()
                        .into(cbirSitesHolder.image);
                //设置Item标题
                cbirSitesHolder.title.setText(item4.getTitle());
                //设置Item尺寸
                cbirSitesHolder.size.setText(item4.getImgSize());
                if (item4.getImgSize().equals("")) {
                    cbirSitesHolder.size.setVisibility(View.GONE);
                }
                //设置Item网址信息
                cbirSitesHolder.domain.setText(item4.getDomain());
                //设置Item其他信息
                cbirSitesHolder.description.setText(item4.getDescriptionl());
                cbirSitesHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.itemonClick(position, mItems);
                    }
                });
                break;
            case HPOI:
                final Hpoiitem item5 = (Hpoiitem) mItems.get(position);
                final HpoiHolder hpoiHolder = (HpoiHolder) holder;
                //设置Item图片
                Glide.with(MyApplication.getContext())
                        .load(item5.getmImgurl())
                        .placeholder(R.drawable.none)
                        .centerCrop()
                        .into(hpoiHolder.image);
                //设置Item文字
                hpoiHolder.title.setText(item5.getmTitle());
                //设置点击事件
                hpoiHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.itemonClick(position, mItems,item5.getUrl());
                    }
                });
                break;
            case HPOIDETAILPAGE:
                final Hpoidetailpageitem item6 = (Hpoidetailpageitem) mItems.get(position);
                final HpoiDetailPageHolder hpoiDetailPageHolder = (HpoiDetailPageHolder) holder;
                //设置Item图片
                Glide.with(MyApplication.getContext())
                        .load(item6.getmImgurl())
                        .placeholder(R.drawable.none)
                        .centerCrop()
                        .into(hpoiDetailPageHolder.image);
                //设置点击事件
                hpoiDetailPageHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.itemonClick(position, mItems);
                    }
                });
                break;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == ITEMZERO) {
            //item==0，里番item布局;
            if (mQieHuan) {
                //一列布局
                view = LayoutInflater
                        .from(parent.getContext()).inflate(R.layout.layout_linear_itemone, parent, false);
            } else if (mQieHuan == false) {
                //两列布局
                view = LayoutInflater
                        .from(parent.getContext()).inflate(R.layout.layout_linear_itemtow, parent, false);
            }

            return new ItemHolder(view);
        } else if (viewType == ITEMONE) {
            //item==1，更多功能item布局
            view = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.more_item, parent, false);
            return new MoreHolder(view);
        } else if (viewType == ITEMTWO) {
            //item==2，Asmritem布局
            view = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.layout_asmr_item, parent, false);
            return new AsmrHolder(view);
        } else if (viewType == ITEMTHREE) {
            //item==3，试炼洞窑item布局
            view = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.layout_testholekiln_item, parent, false);
            return new TestholeHolder(view);
        } else if (viewType == ITEMFUOR) {
            //item==4，搜图item布局
            view = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.layout_cbirsites_item, parent, false);
            return new CbirSitesHolder(view);
        } else if (viewType == ITEMFIVE) {
            //item==5，手办图鉴item布局
            view = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.layout_hpoi_item, parent, false);
            return new HpoiHolder(view);
        } else if (viewType == ITEMSIX) {
            //item==6，手办图鉴详细页item布局
            view = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.layout_hpoidetailpage_item, parent, false);
            return new HpoiDetailPageHolder(view);
        }
        return null;
    }

    private void addTag(ChipGroup chipGroup, JSONArray tagarr) {
        chipGroup.removeAllViews(); // 清除之前的标签
        try {
            for (int i = 0; i < tagarr.length(); i++) {
                JSONObject obj = (JSONObject) tagarr.get(i);

                String tag_id = obj.getString("id");
                String tag = obj.getString("name");
                chipGroup.addView(createChiptext(tag));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Chip createChiptext(String str) {
        Chip chip = new Chip(mcontext);
        chip.setText(str);
        chip.setTextSize(13);
        chip.setCloseIconVisible(false);
        chip.setClickable(true);
        return chip;
    }

    public Filter getFilter() {
        return new Filter() {

            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (results.values == null) {
                    //没有过滤的内容，则使用源数据
                    mItems.clear();
                    mItems.addAll(mCopyInviteMessages);
                }
                //关键字为空的时候，搜索结果为复制的结果
                if (constraint == null || constraint.length() == 0) {
                    results.values = mCopyInviteMessages;
                    results.count = mCopyInviteMessages.size();
                } else {
                    String prefixString = constraint.toString();
                    final int count = mItems.size();
                    //用于存放暂时的过滤结果
                    final ArrayList<MultipleItem> newValues = new ArrayList<MultipleItem>();
                    for (int i = 0; i < count; i++) {
                        final Item value = (Item) mItems.get(i);
                        String username = value.getTitle();
                        if (username.contains(prefixString)) {
                            newValues.add(value);
                        } else {
                            final String[] words = username.split("");
                            final int wordCount = words.length;

                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].contains(prefixString)) {
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
                if (null != results && null != results.values) {
                    mItems.addAll((ArrayList<MultipleItem>) results.values);//将过滤结果添加到这个对象
                }
                if (results.count == 0) {
                    //有关键字的时候刷新数据
                    notifyDataSetChanged();
                } else {
                    //关键字不为零但过滤结果为空刷新数据
                    if (constraint.length() != 0) {
                        notifyDataSetChanged();
                        return;
                    }
                    setFilter((ArrayList<MultipleItem>) mCopyInviteMessages);
                }
            }
        };
    }

    public void setFilter(ArrayList<MultipleItem> filterWords) {
        mCopyInviteMessages = new ArrayList<>();
        this.mCopyInviteMessages.addAll(filterWords);
        this.mItems = filterWords;
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

    class MoreHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;

        MoreHolder(View item) {
            super(item);
            image = item.findViewById(R.id.image_view);
            title = item.findViewById(R.id.text_title);
        }
    }

    public class AsmrHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView id,release,title;
        ChipGroup tags;
        Chip name;

        AsmrHolder(View item) {
            super(item);
            image = item.findViewById(R.id.image_view);
            id = item.findViewById(R.id.text_id);
            release = item.findViewById(R.id.text_release);
            title = item.findViewById(R.id.text_title);
            tags = item.findViewById(R.id.item_tag);
            name = item.findViewById(R.id.tag_name);
        }
    }

    class TestholeHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView duration;

        TestholeHolder(View item) {
            super(item);
            image = item.findViewById(R.id.image_view);
            title = item.findViewById(R.id.text_title);
            duration = item.findViewById(R.id.text_duration);
        }
    }

    class CbirSitesHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView size;
        TextView domain;
        TextView description;

        CbirSitesHolder(View item) {
            super(item);
            image = item.findViewById(R.id.image_view);
            title = item.findViewById(R.id.text_title);
            size = item.findViewById(R.id.text_imgsize);
            domain = item.findViewById(R.id.text_domain);
            description = item.findViewById(R.id.text_description);
        }
    }

    //手办图鉴item
    class HpoiHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;

        HpoiHolder(View item) {
            super(item);
            image = item.findViewById(R.id.image_view);
            title = item.findViewById(R.id.text_title);
        }
    }

    //手办图鉴详细页item
    class HpoiDetailPageHolder extends RecyclerView.ViewHolder {

        ImageView image;

        HpoiDetailPageHolder(View item) {
            super(item);
            image = item.findViewById(R.id.image_view);
        }
    }

    //Item点击接口
    public interface OnItemClickListener {
        void itemonClick(int position, List<MultipleItem> mItems);
        void itemonClick(int position, List<MultipleItem> mItems,String url);
        void itemHoldersonClick(int position);
    }
}
