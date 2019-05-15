package com.lockesec.quizapp401;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


public class DrawerUtil {

    private static String name;
    private static String email;
    private static Uri profilePicUri;

    public static void getDrawer(final Activity activity)
    {
        PrimaryDrawerItem game = new PrimaryDrawerItem().withIdentifier(0).withName("Game").withIcon(R.drawable.ic_quizapp_logo_24dp);
        PrimaryDrawerItem addQuestion = new PrimaryDrawerItem().withIdentifier(1).withName("Add Question").withIcon(R.drawable.ic_question_answer_black_24dp);
        PrimaryDrawerItem editProfile = new PrimaryDrawerItem().withIdentifier(2).withName("Edit Profile").withIcon(R.drawable.ic_profile);
        PrimaryDrawerItem signOut = new PrimaryDrawerItem().withIdentifier(3).withName("Sign Out").withIcon(R.drawable.ic_logout);

        loadUser();
        AccountHeader header = createProfileHeader(activity);

        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(header)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(false)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        game,
                        addQuestion,
                        editProfile,
                        signOut
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d("position ", position + "");

                        switch (position)
                        {
                            case 1:
                                view.getContext().startActivity(new Intent(activity, MainActivity.class));
                                break;

                            case 2:
                                view.getContext().startActivity(new Intent(activity, AddQuestionActivity.class));
                                break;

                            case 3:
                                view.getContext().startActivity(new Intent(activity, ProfileActivity.class));
                                break;

                            case 4:
                                ProfileManager.getInstance().logout();
                                Intent intent = new Intent(activity, SignUpActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                view.getContext().startActivity(intent);
                                break;
                        }
                        return true;
                    }
                })
                .build();
    }

    private static void loadUser()
    {
        name = ProfileManager.getInstance().getUserDisplayName();
        email = ProfileManager.getInstance().getUserEmail();
    }

    public static AccountHeader createProfileHeader(Activity activity)
    {
        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(new ProfileDrawerItem().withName(name).withEmail(email).withIcon(profilePicUri))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        return header;
    }
}