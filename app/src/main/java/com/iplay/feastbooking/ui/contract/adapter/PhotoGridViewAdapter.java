package com.iplay.feastbooking.ui.contract.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.ImageCompressUtility;
import com.iplay.feastbooking.basic.BasicArrayAdapter;
import com.iplay.feastbooking.entity.IdentityMatrix;
import com.iplay.feastbooking.net.utilImpl.contract.ContractUtility;
import com.iplay.feastbooking.ui.contract.data.ContractPhotoPath;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Guyuhan on 2017/11/8.
 */

public class PhotoGridViewAdapter extends BasicArrayAdapter<ContractPhotoPath>{

    private boolean isModified = false;

    private static final int TYPE_PHOTO = 1;
    private static final int TYPE_ADD = 2;

    private IdentityMatrix identityMatrix;

    private static final int IMAGE_WIDTH = 90, IMAGE_HEIGHT = 100;

    private String approvalStatus;

    public PhotoGridViewAdapter(@NonNull Context context, @LayoutRes int resource,
                                @NonNull List<ContractPhotoPath> objects) {
        super(context, resource, objects);
    }

    public PhotoGridViewAdapter(@NonNull Context context, @LayoutRes int resource,
                                @NonNull List<ContractPhotoPath> objects, IdentityMatrix identityMatrix) {
        super(context, resource, objects);
        this.identityMatrix = identityMatrix;
    }

    public PhotoGridViewAdapter(@NonNull Context context, @LayoutRes int resource,
                                @NonNull List<ContractPhotoPath> objects, IdentityMatrix identityMatrix,
                                String approvalStatus){
        this(context, resource, objects, identityMatrix);
        this.approvalStatus = approvalStatus;
    }

    public boolean isModified(){
        return isModified;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.delete_iv = (ImageView) convertView.findViewById(R.id.photo_delete_iv);
            viewHolder.photo_iv = (ImageView) convertView.findViewById(R.id.photo_tiv);
            viewHolder.add_iv = (RoundedImageView) convertView.findViewById(R.id.add_photo_iv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int viewType = getItemViewType(position);
        if(viewType == TYPE_PHOTO){
            if(identityMatrix.isCustomer()
                    && approvalStatus !=null
                    && approvalStatus.equals("PENDING")) {
                viewHolder.delete_iv.setVisibility(View.VISIBLE);
                viewHolder.delete_iv.setOnClickListener(new OnPhotoDeleteClickListener(position));
            }else {
                viewHolder.delete_iv.setVisibility(View.INVISIBLE);
            }
            ImageView tiv = viewHolder.photo_iv;
            tiv.setVisibility(View.VISIBLE);
            ContractPhotoPath path = mGridData.get(position);
            viewHolder.add_iv.setVisibility(View.INVISIBLE);
            if(path.getType() == ContractPhotoPath.TYPE_FROM_INTERNET){
                Glide.with(parent.getContext())
                        .load(path.getUrl())
                        .placeholder(R.drawable.loading)
                        .centerCrop()
                        .thumbnail(0.5f)
                        .dontAnimate()
                        .override(IMAGE_WIDTH, IMAGE_HEIGHT)
                        .into(tiv);
            }else if(path.getType() == ContractPhotoPath.TYPE_FROM_DISK){
                Glide.with(mContext)
                        .load(new File(path.getUrl()))
                        .placeholder(R.drawable.loading)
                        .thumbnail(0.5f)
                        .centerCrop()
                        .dontAnimate()
                        .override(IMAGE_WIDTH, IMAGE_HEIGHT)
                        .into(tiv);
            }
            viewHolder.photo_iv.setOnClickListener(new OnPhotoPreviewClickListener(mContext, position, getPhotoPaths()));
        }else{
            viewHolder.delete_iv.setVisibility(View.GONE);
            viewHolder.photo_iv.setVisibility(View.GONE);
            viewHolder.add_iv.setVisibility(View.VISIBLE);
            viewHolder.add_iv.setOnClickListener((View.OnClickListener) mContext);
        }
        return convertView;
    }

    public void removePhoto(int position){
        if(mGridData.get(position) != null){
            mGridData.remove(position);
            notifyDataSetChanged();
            isModified = true;
        }
    }


    public void addPhoto(ContractPhotoPath path){
        if(path == null){
            return;
        }
        if(identityMatrix != null){
             if(identityMatrix.isCustomer()
                     && mGridData.get(mGridData.size() - 1) == null){
                 int insertIndex = mGridData.size() - 1;
                 mGridData.add(insertIndex, path);
             }
             if(!identityMatrix.isCustomer()
                     && identityMatrix.isManager()
                     && mGridData.get(mGridData.size() -1) != null){
                mGridData.add(path);
             }
             notifyDataSetChanged();
            isModified = true;
        }
    }

    public void upLoadFileList(int orderId, ContractUtility utility) throws Exception {
        List<File> files = new ArrayList<>();
        String filesToDelete = "";
        String imagePath = ContractUtility.getInstance(mContext).getImageResourcePath();
        for(int i=0; i<mGridData.size(); i++){
            ContractPhotoPath path = mGridData.get(i);
            if(path != null){
                String url = path.getUrl();
                File file ;
                if(!url.startsWith("http")){
                    file = ImageCompressUtility.getCompressedImageFile(mContext, url);
                    if(file == null){
                        throw new Exception("File is null");
                    }
                    files.add(file);
                }else {
                    filesToDelete += url.replace(imagePath, "") + ";";
                }
            }
        }
        utility.updateLoadPictures(orderId, filesToDelete, files, mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return mGridData.get(position) == null ? TYPE_ADD : TYPE_PHOTO;
    }

    private ArrayList<String> getPhotoPaths(){
        ArrayList<String> photosPaths = new ArrayList<>();
        for(int i=0; i<mGridData.size(); i++){
            if (mGridData.get(i) != null){
                photosPaths.add(mGridData.get(i).getUrl());
            }
        }
        return photosPaths;
    };

    private class OnPhotoDeleteClickListener implements View.OnClickListener{

        private int position;

        OnPhotoDeleteClickListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.photo_delete_iv){
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("確定要刪除該圖片嗎?");
                DeletePhotoListener deletePhotoListener = new DeletePhotoListener(position);
                builder.setNegativeButton("取消", deletePhotoListener);
                builder.setPositiveButton("確定", deletePhotoListener);
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.pink));
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.blue_hint));
            }
        }
    }

    private class DeletePhotoListener implements DialogInterface.OnClickListener{

        private int position;

        DeletePhotoListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    PhotoGridViewAdapter.this.removePhoto(position);
                    dialog.dismiss();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }
    }

    private static class OnPhotoPreviewClickListener implements View.OnClickListener{

        private WeakReference<Context> contextWeakReference;

        private int position;

        private ArrayList<String> photoPaths;

        OnPhotoPreviewClickListener(Context context, int position, ArrayList<String> photoPaths){
            this.contextWeakReference = new WeakReference<>(context);
            this.position = position;
            this.photoPaths = photoPaths;
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.photo_tiv){
                Context context = contextWeakReference.get();
                if(context != null){
                    PhotoPreview.builder()
                            .setPhotos(photoPaths)
                            .setCurrentItem(position)
                            .start((AppCompatActivity) context);
                }
            }
        }
    }

    private static class ViewHolder{
        ImageView delete_iv;
        ImageView photo_iv;
        RoundedImageView add_iv;
    }

}
