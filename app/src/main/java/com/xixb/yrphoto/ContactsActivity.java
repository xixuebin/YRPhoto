package com.xixb.yrphoto;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ContactsActivity extends ListActivity {

    Context mContext = null;

    /**获取库Phon表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {
            Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };

    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**电话号码**/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**头像ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**联系人的ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;


    /**联系人名称**/
    private ArrayList<String> mContactsName = new ArrayList<>();

    /**联系人头像**/
    private ArrayList<String> mContactsNumber = new ArrayList<>();

    /**联系人头像**/
    private ArrayList<Bitmap> mContactsPhonto = new ArrayList<>();

    ListView mListView = null;
    MyListAdapter myAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = this;
        mListView = this.getListView();
        /**得到手机通讯录联系人信息**/
        getPhoneContacts();

        myAdapter = new MyListAdapter(this);
        setListAdapter(myAdapter);


        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                //调用系统方法拨打电话
                Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri
                        .parse("tel:" + mContactsNumber.get(position)));
                startActivity(dialIntent);
            }
        });

        super.onCreate(savedInstanceState);
    }

    private int getPhonesContactNum(){
        return mContext.getContentResolver().query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null).getCount();
    }
    /**得到手机通讯录联系人信息**/
    private void getPhoneContacts() {

        ContentResolver resolver = mContext.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);

        while (phoneCursor.moveToNext()) {

            //得到手机号码
            String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
            //当手机号码为空的或者为空字段 跳过当前循环
            if (TextUtils.isEmpty(phoneNumber))
                continue;

            //得到联系人名称
            String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

            //得到联系人ID
            Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

            //得到联系人头像ID
            Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

            //得到联系人头像Bitamp
            Bitmap contactPhoto ;

            //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
            if(photoid > 0 ) {
                Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);
                InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                contactPhoto = BitmapFactory.decodeStream(input);
            }else {
                contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.logo3);
            }

            mContactsName.add(contactName);
            mContactsNumber.add(phoneNumber);
            mContactsPhonto.add(contactPhoto);
        }

        phoneCursor.close();
    }


    public boolean addDefaultNum(){
        return insert("defaultUser", "18010889999", "2242358596@qq.com",
                "2242358596");
    }

    /**得到手机SIM卡联系人人信息**/
    private void getSIMContacts() {
        ContentResolver resolver = mContext.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
                null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);

                //Sim卡中没有联系人头像

                mContactsName.add(contactName);
                mContactsNumber.add(phoneNumber);
            }

            phoneCursor.close();
        }
    }

    class MyListAdapter extends BaseAdapter {
        public MyListAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            //设置绘制数量
            return mContactsName.size();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView iamge = null;
            TextView title = null;
            TextView text = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.activity_contacts, null);
                iamge = (ImageView) convertView.findViewById(R.id.color_image);
                title = (TextView) convertView.findViewById(R.id.color_title);
                text = (TextView) convertView.findViewById(R.id.color_text);
            }
            //绘制联系人名称
            assert title != null;
            title.setText(mContactsName.get(position));
            //绘制联系人号码
            text.setText(mContactsNumber.get(position));
            //绘制联系人头像
            iamge.setImageBitmap(mContactsPhonto.get(position));
            return convertView;
        }

    }


    public boolean insert(String given_name, String mobile_number,
                                 String work_email, String im_qq) {
        try {
            ContentValues values = new ContentValues();

            // 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
            Uri rawContactUri = getContentResolver().insert(
                    ContactsContract.RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);

            // 向data表插入姓名数据
            if (!given_name.equals("")) {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, given_name);
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }

            // 向data表插入电话数据
            if (!mobile_number.equals("")) {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile_number);
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }

            // 向data表插入Email数据
            if (!work_email.equals("")) {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Email.DATA, work_email);
                values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }

            // 向data表插入QQ数据
            if (!im_qq.equals("")) {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Im.DATA, im_qq);
                values.put(ContactsContract.CommonDataKinds.Im.PROTOCOL, ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ);
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }
            // 向data表插入头像数据
            Bitmap sourceBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.icon);
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            // 将Bitmap压缩成PNG编码，质量为100%存储
            sourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            byte[] avatar = os.toByteArray();
            values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, avatar);
            getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                    values);
        }

        catch (Exception e) {
            return false;
        }
        return true;
    }

} 