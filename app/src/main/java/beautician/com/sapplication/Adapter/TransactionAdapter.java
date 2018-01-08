package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import beautician.com.sapplication.Activity.Wallet;
import beautician.com.sapplication.Pojo.CategoryList;
import beautician.com.sapplication.Pojo.Transactions;
import beautician.com.sapplication.R;

/**
 * Created by Amaresh on 1/1/18.
 */

public class TransactionAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Transactions> new_list;
    Holder holder;
    public TransactionAdapter(Wallet wallet, ArrayList<Transactions> transactionsList) {
    this._context=wallet;
    new_list=transactionsList;
    }

    @Override
    public int getCount() {
        return new_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class Holder{
        TextView remarks,datetimr,credit,debit;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Transactions _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.trans_list, parent, false);
            holder.remarks=(TextView)convertView.findViewById(R.id.remarks);
            holder.datetimr=(TextView)convertView.findViewById(R.id.datetime);
            holder.credit=(TextView)convertView.findViewById(R.id.credit);
            holder.debit=(TextView)convertView.findViewById(R.id.debit);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();

        }
        holder.remarks.setTag(position);
        holder.datetimr.setTag(position);
        holder.credit.setTag(position);
        holder.debit.setTag(position);

        holder.remarks.setText(_pos.getRemarks());
        holder.datetimr.setText(_pos.getCreated());
        holder.credit.setText("+" +_pos.getCredit());
        holder.debit.setText("-"+ _pos.getDebit());
        return convertView;
    }
}
