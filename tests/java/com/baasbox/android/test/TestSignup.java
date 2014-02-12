package com.baasbox.android.test;

import com.baasbox.android.*;
import com.baasbox.android.test.common.BaasTestBase;

/**
 * Created by Andrea Tortorella on 05/02/14.
 */
public class TestSignup extends BaasTestBase {
    private static final String USER_NAME="username";
    private static final String USER_PASSWORD ="userpass";

    @Override
    protected void beforeTest() throws Exception {
        super.beforeTest();
        resetDb();
    }

    public void testPreconditions(){
        assertNotNull("baasbox not initialized",box);
    }

    public void testSignupNewUser(){
        RequestToken token= BaasUser.withUserName(USER_NAME)
                .setPassword(USER_PASSWORD)
                .signup(new BaasHandler<BaasUser>() {
                    @Override
                    public void handle(BaasResult<BaasUser> result) {

                    }
                });
        BaasResult<BaasUser> await = token.await();
        try {
            BaasUser user = await.get();
            assertNotNull(BaasUser.current());
            assertEquals(user, BaasUser.current());
        } catch (BaasException e) {
            fail(e.toString());
        }
    }

    public void testSignupExistingShouldFail(){
        BaasResult<BaasUser> precedingUser = BaasUser.withUserName(USER_NAME)
                                            .setPassword(USER_PASSWORD)
                                            .signupSync();
        if (precedingUser.isSuccess()){
            BaasResult<BaasUser> await = BaasUser.withUserName(USER_NAME)
                    .setPassword(USER_PASSWORD)
                    .signup(BaasHandler.NOOP)
                    .await();

            assertFalse(await.isSuccess());
        } else {
            fail("unable to create first user");
        }
    }


}
