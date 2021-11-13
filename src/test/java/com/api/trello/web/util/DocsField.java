package com.api.trello.web.util;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class DocsField {

    private String path;
    private JsonFieldType type;
    private String description;

    private static List<DocsField> headerFields = new ArrayList<>(Arrays.asList(
            new DocsField("message", JsonFieldType.STRING, "message"),
            new DocsField("status", JsonFieldType.NUMBER, "status"),
            new DocsField("code", JsonFieldType.NUMBER, "code")
    ));

    public static DocsField of(String path, JsonFieldType type, String description) {
        return new DocsField(path, type, description);
    }

    private DocsField(String path, JsonFieldType type, String description) {
        this.path = path;
        this.type = type;
        this.description = description;
    }

    public static List<FieldDescriptor> makeFields(DocsField... docsFields) {
        return toFieldDescriptors(Arrays.asList(docsFields));
    }

    public static List<FieldDescriptor> makeFieldsWithHeader(DocsField... docsFields) {
        //쌓임 그래서 테스트 여러개하면 Fail 뜨는듯
//        headerFields.addAll(Arrays.asList(docsFields));
        List<DocsField> headerAndDataFields = Stream.of(headerFields, Arrays.asList(docsFields))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return toFieldDescriptors(headerAndDataFields);
    }

    private static List<FieldDescriptor> toFieldDescriptors(List<DocsField> docsFields) {
        return docsFields.stream()
                .map(DocsField::toFieldDescriptor)
                .collect(Collectors.toList());
    }

    private FieldDescriptor toFieldDescriptor() {
        return fieldWithPath(this.path).type(this.type).description(this.description);
    }

}
