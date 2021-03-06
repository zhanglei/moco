package com.github.dreamhead.moco;

import com.github.dreamhead.moco.helper.MocoTestHelper;
import org.apache.http.Header;
import org.apache.http.client.fluent.Request;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.MocoMount.to;
import static com.github.dreamhead.moco.RemoteTestUtils.port;
import static com.github.dreamhead.moco.RemoteTestUtils.remoteUrl;
import static com.github.dreamhead.moco.RemoteTestUtils.root;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MocoFileRootTest {
    private HttpServer server;
    private MocoTestHelper helper;

    @Before
    public void setup() {
        helper = new MocoTestHelper();
        server = httpserver(port(), fileRoot("src/test/resources"));
    }

    @Test
    public void should_config_file_root() throws Exception {
        server.response(file("foo.response"));

        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                assertThat(helper.get(root()), is("foo.response"));
            }
        });
    }

    @Test
    public void should_return_header_from_file_root() throws Exception {
        server.response(header("foo", file("foo.response")));

        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                Header header = Request.Get(root()).execute().returnResponse().getFirstHeader("foo");
                assertThat(header.getValue(), is("foo.response"));
            }
        });
    }

    @Test
    public void should_return_template_header_from_file_root() throws Exception {
        server.response(header("foo", template(file("foo.response"))));

        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                Header header = Request.Get(root()).execute().returnResponse().getFirstHeader("foo");
                assertThat(header.getValue(), is("foo.response"));
            }
        });
    }

    @Test
    public void should_return_template_from_file_root() throws Exception {
        server.response(template(file("foo.response")));

        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                assertThat(helper.get(root()), is("foo.response"));
            }
        });
    }

    @Test
    public void should_mount_correctly() throws Exception {
        server.mount("test", to("/dir"));

        running(server, new Runnable() {
            @Override
            public void run() throws Exception {
                assertThat(helper.get(remoteUrl("/dir/dir.response")), is("response from dir"));
            }
        });
    }
}
