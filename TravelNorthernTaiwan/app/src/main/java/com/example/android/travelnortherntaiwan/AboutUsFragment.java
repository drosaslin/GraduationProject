package com.example.android.travelnortherntaiwan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

public class AboutUsFragment extends android.support.v4.app.Fragment {
    private TextView textViewContent;
    private String arrayName[]={"Daniel",
                                "Genesis",
                                "David"};
    private Button btn;
    private int index2;
    static final DrawerActivity drawerActivity = new DrawerActivity();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textViewContent = getView().findViewById(R.id.textView_content);
        String content = "Every time we think about travelling in Taiwan, Taipei is the first thing " +
                "that comes to our mind because it’s the capital city or maybe because it’s Taiwan’s " +
                "most famous region. However, other regions in Taiwan are worth visiting as well, so " +
                "one of our motivations could be to promote tourism to other regions in the North of " +
                "Taiwan, since it’s near Taipei.\n" +
                "\tAdditionally, travelling might get slightly stressful, especially if we are planning " +
                "the trip by ourselves, so we intend to offer a travel guide that also has useful tools " +
                "such as budget planning, weather information, and a to-do list, so the user won’t " +
                "have to download several apps because all the necessary tools are in the same application.\n";
        //String content = "A";
        textViewContent.setText(content);

        CircleMenu circleMenu = getView().findViewById(R.id.circle_menu);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.ic_group_black_24dp, R.drawable.ic_close_black_24dp)
                .addSubMenu(Color.parseColor("#258CFF"), R.mipmap.ic_boy_round)
                .addSubMenu(Color.parseColor("#FFB3FF"), R.mipmap.ic_girl_round)
                .addSubMenu(Color.parseColor("#0088A8"), R.mipmap.ic_man_round)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {
                        index2 = index;
                        Log.d("circle", "select -> " + arrayName[index]);
                        buildGifDialog(index);
                    }

                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

                    @Override
                    public void onMenuOpened() {}

                    @Override
                    public void onMenuClosed() {}

        });
    }

    private void buildGifDialog(int index){
        String[] title = {"Daniel", "Genesis", "David"};
        String[] introduction = {"Hello! I am Daniel Chen.", "Hello! I am Genesis Yau.", "Hello! I am David Rosas."};
        int[] gif = {R.drawable.gif_dog, R.drawable.gif_cat, R.drawable.gif_guinea};
        new FancyGifDialog.Builder(getActivity())
                .setTitle(title[index])
                .setMessage(introduction[index])
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Send")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(gif[index])   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(getActivity(),"Ok",Toast.LENGTH_SHORT).show();
                        sendEmail();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(getActivity(),"Cancel",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }

    private void sendEmail(){

        String[] email = {"ahm12345@gmail.com", "gyauxd@gmail.com", "davidrosas.xc@gmail.com"};
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , email);
        Log.d("email", "index2 -> " + index2 + "emailAddress -> " + email[index2]);
        i.putExtra(Intent.EXTRA_SUBJECT, "Thanks for using our App!");
        i.putExtra(Intent.EXTRA_TEXT   , "Thanks for using our App!");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
