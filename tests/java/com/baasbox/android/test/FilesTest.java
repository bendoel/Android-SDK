package com.baasbox.android.test;

import com.baasbox.android.*;
import com.baasbox.android.test.common.BaasTestBase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by Andrea Tortorella on 05/02/14.
 */
public class FilesTest extends BaasTestBase {

    private final static String TEXT_FILE =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean auctor dignissim mauris vitae iaculis. Morbi pharetra sem lorem, vel rutrum mauris facilisis sit amet. Curabitur non lacus nec lacus cursus dictum. Ut dictum scelerisque ultricies. Nulla semper nibh at tincidunt blandit. Vestibulum eget suscipit felis, nec sagittis tellus. Nunc a volutpat turpis. Pellentesque et nisi eget nisl interdum vulputate. Aliquam molestie sem nec tempus sodales.\n" +
                    "\n" +
                    "Fusce sollicitudin rhoncus lorem. Nam convallis ut tortor at tempor. Nunc pharetra interdum fringilla. Proin id cursus urna. Donec nec turpis vehicula magna aliquam gravida vel nec dolor. Sed viverra nunc tempus, bibendum velit at, porttitor odio. Proin eu nunc est. Praesent laoreet risus porta varius condimentum. Nullam tincidunt erat felis, ac facilisis nisl faucibus sit amet. Nam vulputate ipsum eu lorem pellentesque suscipit. Vestibulum nisl mauris, lacinia ut erat sit amet, vestibulum tempus lacus.\n" +
                    "\n" +
                    "Curabitur ut nunc suscipit, sodales magna at, laoreet felis. Fusce pretium volutpat lacus et suscipit. Sed et nibh tristique, vulputate quam sed, consequat lacus. Vestibulum id feugiat nulla, at blandit est. Donec sit amet porta enim. Nullam sit amet felis in metus eleifend ornare. Nulla facilisis, lacus a sagittis malesuada, dui erat aliquet mi, sed rutrum purus tellus eget mauris.\n" +
                    "\n" +
                    "Donec suscipit pellentesque gravida. Etiam a diam congue, accumsan nisl et, lobortis metus. Maecenas vel sapien sollicitudin, consequat urna sit amet, tempor nisi. Morbi id congue lorem. In rhoncus, nunc quis elementum feugiat, neque orci venenatis ante, nec blandit nibh neque consectetur sem. Nunc nec tincidunt lacus. Vivamus eu massa mollis, lobortis sem eget, ultrices nisl. Aliquam erat volutpat.\n" +
                    "\n" +
                    "Integer euismod consectetur lacus sed fermentum. Maecenas magna neque, venenatis quis est sit amet, molestie vehicula tortor. Nulla ut fermentum tortor, et feugiat mauris. Aenean faucibus elit lorem. Integer ultrices lacus quis rutrum placerat. Cras hendrerit neque sit amet lobortis accumsan. Donec non tincidunt lorem.";

    private String fileId;
    @Override
    protected void beforeClass() throws Exception {
        super.beforeClass();
        resetDb();
        BaasResult<BaasUser> res = BaasUser.withUserName("test")
                .setPassword("test")
                .signupSync();
        assertTrue(res.isSuccess());
    }

    @Override
    protected void beforeTest() throws Exception {
        super.beforeTest();
        BaasFile test = new BaasFile();
        BaasResult<BaasFile> res = test.uploadSync(getStringInput());
        BaasFile file = res.get();
        fileId = file.getId();
    }

    public void testStream(){
        stream();
    }


    public void testStreamTwo(){
        stream();
        stream();
    }

    private void stream() {
        RequestToken t = BaasFile.fetchStream(fileId, new BaasHandler<BaasFile>() {
            @Override
            public void handle(BaasResult<BaasFile> result) {

            }
        });
        BaasResult<BaasFile> await = t.await();
        try {
            BaasFile f = await.get();
            assertNotNull(f.getId());
            assertNotNull(f.getData());
        } catch (BaasException e) {
            e.printStackTrace();
        }
    }

    public void testUpload(){
        BaasFile file = new BaasFile();
        RequestToken token=file.upload(getStringInput(), new BaasHandler<BaasFile>() {
            @Override
            public void handle(BaasResult<BaasFile> result) {

            }
        });
        BaasResult<BaasFile> w = token.await();
        try {
            BaasFile fileRes =w.get();
            assertNotNull(fileRes.getId());
        } catch (BaasException e) {
            fail(e.getMessage());
        }
    }

    private InputStream getStringInput(){
        return new ByteArrayInputStream(TEXT_FILE.getBytes(Charset.defaultCharset()));
    }


}
