package com.lockesec.quizapp401;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

public class DrawerUtil  {

    public static void getDrawer(final Activity activity)
    {
        PrimaryDrawerItem game = new PrimaryDrawerItem().withIdentifier(0).withName("Game").withTextColorRes(R.color.login_form).withIcon(R.drawable.ic_quizapp_logo_24dp);
        PrimaryDrawerItem addQuestion = new PrimaryDrawerItem().withIdentifier(1).withName("Add Question").withTextColorRes(R.color.login_form).withIcon(R.drawable.ic_question_answer_white_24dp);
        PrimaryDrawerItem editProfile = new PrimaryDrawerItem().withIdentifier(2).withName("Edit Profile").withTextColorRes(R.color.login_form).withIcon(R.drawable.ic_profile_circle_white_24dp);
        PrimaryDrawerItem checkLeaderboard = new PrimaryDrawerItem().withIdentifier(3).withName("Check Leaderboard").withTextColorRes(R.color.login_form).withIcon(R.drawable.ic_leaderboard_white_24dp);
        PrimaryDrawerItem signOut = new PrimaryDrawerItem().withIdentifier(4).withName("Sign Out").withTextColorRes(R.color.login_form).withIcon(R.drawable.ic_logout_white_24dp);

        AccountHeader header = createProfileHeader(activity);

        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(header)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(false)
                .withActionBarDrawerToggleAnimated(true)
                .withSliderBackgroundDrawableRes(R.drawable.bg)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        game,
                        addQuestion,
                        editProfile,
                        checkLeaderboard,
                        signOut
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position)
                        {
                            case 1:
                                if(activity.getClass() != MainActivity.class)
                                    view.getContext().startActivity(new Intent(activity, MainActivity.class));
                                break;
                            case 2:
                                if(activity.getClass() != AddQuestionActivity.class)
                                    view.getContext().startActivity(new Intent(activity, AddQuestionActivity.class));
                                break;

                            case 3:
                                if(activity.getClass() != ProfileActivity.class)
                                    view.getContext().startActivity(new Intent(activity, ProfileActivity.class));
                                break;

                            case 4:
                                if(activity.getClass() != LeaderboardActivity.class)
                                    view.getContext().startActivity(new Intent(activity, LeaderboardActivity.class));
                                break;

                            case 5:
                                FirebaseAuth.getInstance().signOut();
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



    public static AccountHeader createProfileHeader(Activity activity)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        String email = user.getEmail();
//        String profilePicURL = user.getPhotoUrl().toString();

//        Log.d("Profile pic: ", profilePicURL);

        ProfileDrawerItem profile = new ProfileDrawerItem();
        profile.withName(name).withEmail(email).withIcon(R.drawable.camera);

        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.color.material_drawer_dark_background)
                .addProfiles(profile)
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
