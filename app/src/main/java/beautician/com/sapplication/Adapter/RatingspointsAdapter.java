package beautician.com.sapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import beautician.com.sapplication.Activity.GiveCommentActivity;
import beautician.com.sapplication.Pojo.RatingsPoints;
import beautician.com.sapplication.R;

/**
 * Created by Amaresh on 11/27/17.
 */

public  class RatingspointsAdapter extends ArrayAdapter {
    ArrayList<RatingsPoints> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
    }

    public RatingspointsAdapter(ArrayList data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public RatingsPoints getItem(int position) {
        return  dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            result = convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        RatingsPoints item = getItem(position);


        viewHolder.txtName.setText(item.getPoints());
        viewHolder.checkBox.setTag(position);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                dataSet.get(getPosition).setIschecked(buttonView.isChecked());


                /*if(getContext() instanceof GiveCommentActivity) {
                    ((GiveCommentActivity) getContext()).onItemClickOfListView(getPosition, buttonView.isChecked());
                }*/
            }
        });
        viewHolder.checkBox.setChecked(dataSet.get(position).getIschecked());

        return result;
    }
}
