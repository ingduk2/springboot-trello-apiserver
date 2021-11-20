package com.api.trello.web.board.controller;

import com.api.trello.web.board.dto.BoardResponseDto;
import com.api.trello.web.board.dto.BoardSaveRequestDto;
import com.api.trello.web.board.dto.BoardUpdateRequestDto;
import com.api.trello.web.board.exception.CBoardNotFoundException;
import com.api.trello.web.board.service.BoardService;
import com.api.trello.web.util.ControllerTestUtil;
import com.api.trello.web.util.DocsField;
import com.api.trello.web.workspace.exception.CWorkspaceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({RestDocumentationExtension.class})
@WebMvcTest(BoardController.class)
class BoardControllerTest {

    private static String BOARD_URL = "/boards";

    MockMvc mvc;

    @MockBean
    BoardService boardService;

    @BeforeEach
    void setUp(WebApplicationContext ctx, RestDocumentationContextProvider restCtx) {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restCtx))
//                .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    private List<BoardResponseDto> getBoardResponseDtos() {
        List<BoardResponseDto> list = new ArrayList<>();
        for (int i = 1; i <= 10 ; i++) {
            list.add(getBoardResponseDto((long) i, "workspace" + i));
        }

        return list;
    }

    private BoardResponseDto getBoardResponseDto(Long id, String title) {
        return BoardResponseDto.builder()
                .boardId(id)
                .title(title)
                .build();

    }

    @Test
    void GET_findAllBoard_성공() throws Exception {
        //given
        Long workspaceId = 1L;
        given(boardService.findAll(eq(workspaceId)))
                .willReturn(getBoardResponseDtos());

        //when
        final ResultActions actions = mvc.perform(
                get("/workspaces/1" + BOARD_URL))
                .andDo(print())
                .andDo(document("boards-findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                DocsField.makeFieldsWithHeader(DocsField.of("data.[].boardId", JsonFieldType.NUMBER, "boardId"),
                                        DocsField.of("data.[].title", JsonFieldType.STRING, "boardTitle")))
                        ));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(10));
    }

    @Test
    void GET_findBoardById_성공() throws Exception {
        //given
        Long boardId = 1L;
        String title = "boardTitle";

        given(boardService.findBoardResponseDtoById(boardId))
                .willReturn(getBoardResponseDto(boardId, title));

        //when
        final ResultActions actions = mvc.perform(
                RestDocumentationRequestBuilders.get(BOARD_URL + "/{boardId}", boardId))
                .andDo(print())
                .andDo(document("boards-findOne",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("boardId").description("boardId")
                        ),
                        responseFields(DocsField.makeFieldsWithHeader(DocsField.of("data.boardId", JsonFieldType.NUMBER, "boardId"),
                                        DocsField.of("data.title", JsonFieldType.STRING, "boardTitle")))
                ));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.boardId").value(boardId))
                .andExpect(jsonPath("$.data.title").value(title));
    }

    @Test
    void GET_Board_CBoardNotFoundException() throws Exception {
        //given
        Long boardId = 100L;
        String title = "boardTitle";

        given(boardService.findBoardResponseDtoById(boardId))
                .willThrow(new CBoardNotFoundException());

        //when
        final ResultActions actions = mvc.perform(
                get(BOARD_URL + "/" + boardId))
                .andDo(print());

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(ControllerTestUtil.getApiResultExceptionClass(result))
                                .isEqualTo(CBoardNotFoundException.class));
    }

    @Test
    void POST_saveBoard_성공() throws Exception {
        //given
        Long workspaceId = 1L;
        String title = "board";

        Long boardId = 10L;

        BoardSaveRequestDto requestDto = BoardSaveRequestDto.builder()
                .workspaceId(workspaceId)
                .title(title)
                .build();

        given(boardService.save(any(BoardSaveRequestDto.class)))
                .willReturn(getBoardResponseDto(boardId, title));

        //when
        final ResultActions actions = mvc.perform(
                post(BOARD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(ControllerTestUtil.getJsonStringByDto(requestDto)))
                .andDo(print())
                .andDo(document("boards-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                DocsField.makeFields(
                                        DocsField.of("workspaceId", JsonFieldType.NUMBER, "workspaceId"),
                                        DocsField.of("title", JsonFieldType.STRING, "boardTitle"))
                        ),
                        responseFields(
                                DocsField.makeFieldsWithHeader(
                                        DocsField.of("data.boardId", JsonFieldType.NUMBER, "boardId"),
                                        DocsField.of("data.title", JsonFieldType.STRING, "boardTitle")
                                )
                        )));

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "http://localhost:8080" + BOARD_URL + "/" + boardId))
                .andExpect(jsonPath("$.data.boardId").value(boardId))
                .andExpect(jsonPath("$.data.title").value(title));
    }

    @Test
    void POST_saveBoard_workspace_정보가_없다() throws Exception {
        //given
        Long workspaceId = 8L;
        String boardTitle = "board";
        Long boardId = 5L;

        BoardSaveRequestDto requestDto = BoardSaveRequestDto.builder()
                .workspaceId(workspaceId)
                .title(boardTitle)
                .build();

        given(boardService.save(any(BoardSaveRequestDto.class)))
                .willThrow(new CWorkspaceNotFoundException());

        //when
        final ResultActions actions = mvc.perform(
                post(BOARD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(ControllerTestUtil.getJsonStringByDto(requestDto)))
                .andDo(print());

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(ControllerTestUtil.getApiResultExceptionClass(result))
                                .isEqualTo(CWorkspaceNotFoundException.class));
    }

    @Test
    void PUT_updateBoard_성공() throws Exception {
        //given
        Long boardId = 1L;
        String boardUpdateTitle = "boardUU";
        BoardUpdateRequestDto requestDto = BoardUpdateRequestDto.builder()
                .title(boardUpdateTitle)
                .build();

        given(boardService.update(eq(boardId), any(BoardUpdateRequestDto.class)))
                .willReturn(getBoardResponseDto(boardId, boardUpdateTitle));

        //when
        final ResultActions actions = mvc.perform(
                RestDocumentationRequestBuilders.put(BOARD_URL + "/{boardId}", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(ControllerTestUtil.getJsonStringByDto(requestDto)))
                .andDo(print())
                .andDo(document("boards-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("boardId").description("boardId")
                        ),
                        requestFields(
                                DocsField.makeFields(
                                        DocsField.of("title", JsonFieldType.STRING, "updateboardTitle")
                                )
                        ),
                        responseFields(
                                DocsField.makeFieldsWithHeader(
                                        DocsField.of("data.boardId", JsonFieldType.NUMBER, "boardId"),
                                        DocsField.of("data.title", JsonFieldType.STRING, "boardTitle")
                                )
                        )));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(boardUpdateTitle));
    }

    @Test
    void PUT_updateBoard_Board_정보가_없다() throws Exception {
        //given
        Long boardId = 1L;
        String boardUpdateTitle = "updateBoard";
        BoardUpdateRequestDto requestDto = BoardUpdateRequestDto.builder()
                .title(boardUpdateTitle)
                .build();

        given(boardService.update(eq(boardId), any(BoardUpdateRequestDto.class)))
                .willThrow(new CBoardNotFoundException());

        //when
        final ResultActions actions = mvc.perform(
                put(BOARD_URL + "/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(ControllerTestUtil.getJsonStringByDto(requestDto)))
                .andDo(print());

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(ControllerTestUtil.getApiResultExceptionClass(result))
                                .isEqualTo(CBoardNotFoundException.class));
    }

    @Test
    void DELETE_board_성공() throws Exception {
        //given
        Long deleteBoardId = 5L;

        given(boardService.delete(deleteBoardId))
                .willReturn(deleteBoardId);

        //when
        final ResultActions actions = mvc.perform(
                RestDocumentationRequestBuilders.delete(BOARD_URL + "/{boardId}", deleteBoardId))
                .andDo(print())
                .andDo(document("boards-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("boardId").description("delete BoardId")
                        )));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204));
    }

    @Test
    void DELETE_boards_board_를_찾을_수_없다() throws Exception {
        //given
        Long deleteBoardId = 5L;

        given(boardService.delete(deleteBoardId))
                .willThrow(new CBoardNotFoundException());

        //when
        final ResultActions actions = mvc.perform(
                delete(BOARD_URL + "/" + deleteBoardId))
                .andDo(print());

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(ControllerTestUtil.getApiResultExceptionClass(result))
                                .isEqualTo(CBoardNotFoundException.class));
    }

}