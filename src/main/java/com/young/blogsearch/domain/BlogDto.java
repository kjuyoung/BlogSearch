package com.young.blogsearch.domain;

import com.young.blogsearch.common.modelmapper.CustomMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
public class BlogDto implements Serializable {

    private Meta meta;
    private List<Documents> documents;

    @Builder
    public BlogDto(Meta meta, List<Documents> documents) {
        this.meta = meta;
        this.documents = documents;
    }

    public static BlogDto from(Response response) {
        return CustomMapper.INSTANCE.map(response);
    }

    @Getter
    @Builder
    public static class Meta {
        private int totalCount;
        private int pageableCount;
        private boolean end;
    }

    @Getter
    @Builder
    public static class Documents {
        private String title;
        private String contents;
        private String url;
        private String blogName;
        private String thumbnail;
        private String datetime;
    }
}
