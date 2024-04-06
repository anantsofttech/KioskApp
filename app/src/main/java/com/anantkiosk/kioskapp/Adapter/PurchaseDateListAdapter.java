package com.anantkiosk.kioskapp.Adapter;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anantkiosk.kioskapp.Model.GiftCardModel;
import com.anantkiosk.kioskapp.R;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;

import java.util.ArrayList;

public class PurchaseDateListAdapter extends RecyclerView.Adapter<PurchaseDateListAdapter.MyViewHolder> {
    private ArrayList<GiftCardModel> progress;
    private Activity activity;

    int type;

    public PurchaseDateListAdapter(ArrayList<GiftCardModel> progress, Activity activity, int type) {
        this.progress = progress;
        this.activity = activity;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item_category, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final GiftCardModel progress1 = progress.get(position);
        //holder.title.setText(Html.fromHtml(progress1.getCategory_name()));
        String pType=progress1.getProgramType();
        try {
            if (pType.contains("|"))
                pType=pType.replace("|", "");

            pType=pType.trim();
        }catch (Exception e){

        }
        holder.title.setText(pType);
        holder.title.setVisibility(View.VISIBLE);
        holder.tv_date.setText(progress1.getInvoiceDate());
        if(pType!=null) {
            if(pType.trim().length()>0) {
                if (pType.toLowerCase().contains("online"))
                    holder.title.setTextColor(activity.getResources().getColor(R.color.green));
                else {
                    if (type == 1) {
                        holder.title.setTextColor(activity.getResources().getColor(R.color.clr_purchase));
                    } else {
                        holder.title.setTextColor(activity.getResources().getColor(R.color.clr_loyalty));
                    }
                }
            }else{
                holder.title.setVisibility(View.GONE);
            }
        }else{
            holder.title.setVisibility(View.GONE);
        }
        PurchaseItemListAdapter adapter = new PurchaseItemListAdapter(progress1.getArrProducts(), type, activity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        holder.rvItemList.setLayoutManager(layoutManager);
        holder.rvItemList.setAdapter(adapter);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(activity, R.drawable.dividerdrawable));
        holder.rvItemList.addItemDecoration(dividerItemDecoration);
        holder.rvItemList.setNestedScrollingEnabled(false);
        holder.rvItemList.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        if (progress == null)
            return 0;
        return progress.size();
    }

    public void updateList(ArrayList<GiftCardModel> temp) {
        progress = temp;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, tv_date;
        RecyclerView rvItemList;

        public MyViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.tvType);
            tv_date = view.findViewById(R.id.tv_date);
            tv_date.setTypeface(UtilsGlobal.setFontSemiBold(activity));
            tv_date.setTypeface(UtilsGlobal.setFontSemiBold(activity));
            rvItemList = view.findViewById(R.id.rvItemlist);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class DividerItemDecorator extends RecyclerView.ItemDecoration {

        private Drawable mDivider;
        private final Rect mBounds = new Rect();

        public DividerItemDecorator(Drawable divider) {
            mDivider = divider;
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            canvas.save();
            final int left;
            final int right;
            if (parent.getClipToPadding()) {
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();
                canvas.clipRect(left, parent.getPaddingTop(), right,
                        parent.getHeight() - parent.getPaddingBottom());
            } else {
                left = 0;
                right = parent.getWidth();
            }

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                final int top = bottom - mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            canvas.restore();
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
                outRect.setEmpty();
            } else
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }
}
