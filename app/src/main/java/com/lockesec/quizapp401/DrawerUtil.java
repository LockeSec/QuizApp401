package com.lockesec.quizapp401;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


public class DrawerUtil {

    public static void getDrawer(final Activity activity, Toolbar toolbar) {
        PrimaryDrawerItem game = new PrimaryDrawerItem().withIdentifier(0).withName("Game").withIcon(R.drawable.ic_profile);
        PrimaryDrawerItem editProfile = new PrimaryDrawerItem().withIdentifier(1).withName("Edit Profile").withIcon(R.drawable.ic_profile);
        PrimaryDrawerItem signOut = new PrimaryDrawerItem().withIdentifier(2).withName("Sign Out").withIcon(R.drawable.ic_profile);

//        AccountHeader header = createProfileHeader(activity, "Rony", "rony@gmail.com");

        String name = "Rony";
        String email = "rony@gmail.com";

        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(new ProfileDrawerItem().withName(name).withEmail(email).withIcon(R.drawable.camera))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        game,
                        editProfile,
                        signOut
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d("position ", position + "");

                        switch (position) {
                            case 0:
                                view.getContext().startActivity(new Intent(activity, MainActivity.class));
                                break;

                            case 1:
                                view.getContext().startActivity(new Intent(activity, ProfileActivity.class));
                                break;

                        }
                        return true;
                    }
                })
                .build();

        result.setHeader(header.getView());
    }
}
//
//    public static AccountHeader createProfileHeader(Activity activity, String name, String email) //Uri profilePictureURI)
//    {
//        AccountHeader header = new AccountHeaderBuilder()
//                .withActivity(activity)
////                .withHeaderBackground(R.drawable.material_drawer_shadow_right)
//                .addProfiles(new ProfileDrawerItem().withName(name).withEmail(email).withIcon(R.drawable.camera))
//                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                    @Override
//                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
//                        return false;
//                    }
//                })
//                .build();
//
//        return header;
//    }
//}