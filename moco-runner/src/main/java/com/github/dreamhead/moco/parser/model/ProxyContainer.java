package com.github.dreamhead.moco.parser.model;

import com.github.dreamhead.moco.handler.failover.Failover;
import com.github.dreamhead.moco.handler.proxy.ProxyConfig;
import com.google.common.base.Objects;

import static com.github.dreamhead.moco.Moco.failover;
import static com.github.dreamhead.moco.Moco.from;
import static com.github.dreamhead.moco.Moco.playback;

public class ProxyContainer {
    private String url;
    private String from;
    private String to;

    private String failover;
    private String playback;

    public String getUrl() {
        return url;
    }

    public boolean hasUrl() {
        return url != null;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(ProxyContainer.class)
                .add("url", this.url)
                .add("failover", this.failover)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Failover getFailover() {
        if (failover != null) {
            return failover(failover);
        }

        if (playback != null) {
            return playback(playback);
        }

        return Failover.DEFAULT_FAILOVER;
    }

    public ProxyConfig getProxyConfig() {
        return from(from).to(to);
    }

    public boolean hasProxyConfig() {
        return from != null && to != null;
    }

    public static class Builder {
        private String url;
        private String failover;
        private String playback;

        private String from;
        private String to;

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withFrom(String from) {
            this.from = from;
            return this;
        }

        public Builder withTo(String to) {
            this.to = to;
            return this;
        }

        public Builder withFailover(String failover) {
            this.failover = failover;
            return this;
        }

        public Builder withPlayback(String playback) {
            this.playback = playback;
            return this;
        }

        public ProxyContainer build() {
            if (this.url != null && (this.from != null || this.to != null)) {
                throw new IllegalArgumentException("Proxy cannot be set in multiple mode");
            }

            if (this.url == null && (this.from == null || this.to == null)) {
                throw new IllegalArgumentException("Batch proxy needs both 'from' and 'to'");
            }


            ProxyContainer container = new ProxyContainer();
            container.url = url;
            container.from = from;
            container.to = to;
            container.failover = failover;
            container.playback = playback;
            return container;
        }
    }
}
