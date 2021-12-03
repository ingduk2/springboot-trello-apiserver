package com.api.trello.web.list.controller;

import com.api.trello.web.list.dto.ListsIndexUpdateDto;
import com.api.trello.web.list.dto.ListsResponseDto;
import com.api.trello.web.list.dto.ListsSaveRequestDto;
import com.api.trello.web.list.dto.ListsUpdateRequestDto;
import com.api.trello.web.list.service.ListsService;
import com.api.trello.web.util.ControllerTestUtil;
import com.api.trello.web.util.DocsField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({RestDocumentationExtension.class})
@WebMvcTest(ListsController.class)
class ListsControllerTest {

    MockMvc mvc;

    @MockBean
    ListsService listsService;

    @BeforeEach
    void setUp(WebApplicationContext ctx, RestDocumentationContextProvider restCtx) {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restCtx))
                .build();
    }

    private List<ListsResponseDto> getListsResponseDtos(Long boardId) {
        List<ListsResponseDto> list = new ArrayList<>();
        for (long listId = 1; listId <= 10; listId++) {
            list.add(getListsResponseDto(listId, boardId, "listTitle" + listId));
        }

        return list;
    }

    private ListsResponseDto getListsResponseDto(Long listId, Long boardId, String listTitle) {
        return ListsResponseDto.builder()
                .listId(listId)
                .boardId(boardId)
                .listTitle(listTitle)
                .index(1L)
                .build();
    }

    @Test
    @DisplayName("GET /lists/{listId} - Id로 list 단일 상세 검색")
    void findById() throws Exception {
        //given
        Long listId = 2L;
        Long boardId = 1L;
        String listTitle = "findOneList";
        given(listsService.findResponseDto(listId))
                .willReturn(getListsResponseDto(listId, boardId, listTitle));

        //when
        ResultActions actions = mvc.perform(
                        RestDocumentationRequestBuilders.get("/lists/{listId}", listId))
                .andDo(print())
                .andDo(document("lists-findById",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("listId").description("listId")
                        ),
                        responseFields(
                                DocsField.makeFieldsWithHeader(
                                        DocsField.of("data.listId", JsonFieldType.NUMBER, "listId"),
                                        DocsField.of("data.boardId", JsonFieldType.NUMBER, "boardId"),
                                        DocsField.of("data.listTitle", JsonFieldType.STRING, "listTitle"),
                                        DocsField.of("data.index", JsonFieldType.NUMBER, "index")))
                ));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.listId").value(listId))
                .andExpect(jsonPath("$.data.boardId").value(boardId))
                .andExpect(jsonPath("$.data.listTitle").value(listTitle));
    }

    @Test
    @DisplayName("GET /boards/{boardId}/lists - boardId pathVariable 로 lists 를 index asc 순으로 조회한다.")
    void findAllListsIndexAsc() throws Exception {
        //given
        Long boardId = 1L;
        given(listsService.findAllIndexAsc(boardId))
                .willReturn(getListsResponseDtos(boardId));

        //when
        ResultActions actions = mvc.perform(
                        RestDocumentationRequestBuilders.get("/boards/{boardId}/lists", boardId))
                .andDo(print())
                .andDo(document("lists-findAll-IndexAsc",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("boardId").description("boardId")
                        ),
                        responseFields(
                                DocsField.makeFieldsWithHeader(
                                        DocsField.of("data.[].listId", JsonFieldType.NUMBER, "listId"),
                                        DocsField.of("data.[].boardId", JsonFieldType.NUMBER, "listId"),
                                        DocsField.of("data.[].listTitle", JsonFieldType.STRING, "listId"),
                                        DocsField.of("data.[].index", JsonFieldType.NUMBER, "listId")
                                )
                        )
                ));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(10));
    }

    @Test
    @DisplayName("POST /lists - list 하나를 저장하는지 확인한다.")
    void save() throws Exception {
        //given
        Long boardId = 1L;
        Long listId = 5L;
        String listTitle = "savedList";

        ListsSaveRequestDto saveRequestDto = ListsSaveRequestDto.builder()
                .boardId(boardId)
                .listTitle(listTitle)
                .build();

        given(listsService.save(any(ListsSaveRequestDto.class)))
                .willReturn(getListsResponseDto(listId, boardId, listTitle));

        //when
        ResultActions actions = mvc.perform(
                        post("/lists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(ControllerTestUtil.getJsonStringByDto(saveRequestDto)))
                .andDo(print())
                .andDo(document("lists-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                DocsField.makeFields(
                                        DocsField.of("boardId", JsonFieldType.NUMBER, "boardId"),
                                        DocsField.of("listTitle", JsonFieldType.STRING, "listTitle")
                                )
                        ),
                        responseFields(
                                DocsField.makeFieldsWithHeader(
                                        DocsField.of("data.listId", JsonFieldType.NUMBER, "listId"),
                                        DocsField.of("data.boardId", JsonFieldType.NUMBER, "listId"),
                                        DocsField.of("data.listTitle", JsonFieldType.STRING, "listId"),
                                        DocsField.of("data.index", JsonFieldType.NUMBER, "listId")
                                )
                        )
                ));

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "http://localhost:8080" + "/lists/" + listId))
                .andExpect(jsonPath("$.data.listId").value(listId))
                .andExpect(jsonPath("$.data.boardId").value(boardId))
                .andExpect(jsonPath("$.data.listTitle").value(listTitle))
                .andExpect(jsonPath("$.data.index").value(1));
    }

    @Test
    @DisplayName("PUT /lists/{listId} - 요청시 listId에 해당하는 것을 update한다.")
    void update() throws Exception {
        //given
        Long listId = 1L;
        Long boardId = 2L;
        String updateListTitle = "updateTitle";

        ListsUpdateRequestDto requestDto = ListsUpdateRequestDto.builder()
                .listTitle(updateListTitle)
                .build();

        given(listsService.update(eq(listId), any(ListsUpdateRequestDto.class)))
                .willReturn(ListsResponseDto.builder()
                        .listId(listId)
                        .boardId(boardId)
                        .listTitle(updateListTitle)
                        .index(1L)
                        .build());
        //when
        ResultActions actions = mvc.perform(
                        RestDocumentationRequestBuilders.put("/lists/{listId}", listId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(ControllerTestUtil.getJsonStringByDto(requestDto)))
                .andDo(print())
                .andDo(document("lists-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("listId").description("listId")
                        ),
                        requestFields(
                                DocsField.makeFields(
                                        DocsField.of("listTitle", JsonFieldType.STRING, "updateListTitle")
                                )
                        ),
                        responseFields(
                                DocsField.makeFieldsWithHeader(
                                        DocsField.of("data.listId", JsonFieldType.NUMBER, "listId"),
                                        DocsField.of("data.boardId", JsonFieldType.NUMBER, "listId"),
                                        DocsField.of("data.listTitle", JsonFieldType.STRING, "listId"),
                                        DocsField.of("data.index", JsonFieldType.NUMBER, "listId")
                                )
                        )));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.listTitle").value(updateListTitle));

    }

    @Test
    @DisplayName("PUT /lists/index - fromId, toId 를 받아서 두개의 인덱스를 바꾼다")
    void updateIndex() throws Exception {
        //given
        Long fromListId = 1L;
        Long toListId = 2L;
        Long boardId = 3L;
        String fromListTitle = "fromListTitle";
        Long fromIndex = 4L;
        Long toIndex = 5L;

        ListsIndexUpdateDto requestDto = ListsIndexUpdateDto.builder()
                .fromListId(fromListId)
                .toListId(toListId)
                .build();

        given(listsService.updateOrder(any(ListsIndexUpdateDto.class)))
                .willReturn(ListsResponseDto.builder()
                        .listId(fromListId)
                        .boardId(boardId)
                        .listTitle(fromListTitle)
                        .index(toIndex)
                        .build());
        //when
        ResultActions actions = mvc.perform(
                        put("/lists/index")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(ControllerTestUtil.getJsonStringByDto(requestDto)))
                .andDo(print())
                .andDo(document("lists-index-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                DocsField.makeFields(
                                        DocsField.of("fromListId", JsonFieldType.NUMBER, "fromListId"),
                                        DocsField.of("toListId", JsonFieldType.NUMBER, "toListId")
                                )
                        ),
                        responseFields(
                                DocsField.makeFieldsWithHeader(
                                        DocsField.of("data.listId", JsonFieldType.NUMBER, "listId"),
                                        DocsField.of("data.boardId", JsonFieldType.NUMBER, "listId"),
                                        DocsField.of("data.listTitle", JsonFieldType.STRING, "listId"),
                                        DocsField.of("data.index", JsonFieldType.NUMBER, "listId")
                                )
                        )
                ));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.listId").value(fromListId))
                .andExpect(jsonPath("$.data.index").value(toIndex));
    }

    @Test
    @DisplayName("DELETE /lists/{listId} - listId 에 해당하는 데이터를 삭제한다.")
    void delete() throws Exception {
        //given
        Long deleteListId = 5L;

        given(listsService.delete(deleteListId))
                .willReturn(deleteListId);

        //when
        ResultActions actions = mvc.perform(
                        RestDocumentationRequestBuilders.delete("/lists/{listId}", deleteListId))
                .andDo(print())
                .andDo(document("lists-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("listId").description("delete listId")
                        )
                ));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204));
    }

}