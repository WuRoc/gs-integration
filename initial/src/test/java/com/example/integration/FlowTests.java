package com.example.integration;

import com.rometools.rome.feed.synd.SyndEntryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @ClassName FlowTests
 * @Description TODO
 * @Author XiaoShuMu
 * @Version 1.0
 * @Create 2021-11-05 16:44
 * @Blog https://www.cnblogs.com/WLCYSYS/
 **/
@SpringBootTest({"auto.startup=false", // we don't want to start the real feed
        "feed.file.name=Test"      // use a different file
})
public class FlowTests {


    private SourcePollingChannelAdapter newsAdapter;

    @Autowired
    private MessageChannel news;


    @Test
    public void test() throws Exception {
        assertThat(this.newsAdapter.isRunning()).isFalse();
        SyndEntryImpl syndEntry = new SyndEntryImpl();
        syndEntry.setTitle("Test Title");
        syndEntry.setLink("http://characters/frodo");
        File out = new File("E:\\tmp\\si\\Test");
        out.delete();
        assertThat(out.exists()).isFalse();
        this.news.send(MessageBuilder.withPayload(syndEntry).build());
        assertThat(out.exists()).isTrue();
        BufferedReader br = new BufferedReader(new FileReader(out));
        String line = br.readLine();
        assertThat(line).isEqualTo("Test Title @ http://characters/frodo");
        br.close();
        out.delete();
    }

}
