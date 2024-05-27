package com.example.hello.domain.book.v10

import com.example.hello.domain.chat.dto.ChatDTO
import com.example.hello.domain.chat.entity.Chat
import com.example.hello.domain.chat.service.ChatService
import lombok.extern.slf4j.Slf4j
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import reactor.util.function.Tuples
import java.net.URI
import java.time.Duration

@Slf4j
@Component
class ChatHandler(
//    private val mapper: BookMapper,
    private val chatService: ChatService
) {
    private val LOCAL_URL = "http://localhost:8080"

    fun createChats(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(ChatDTO.Chat::class.java) //                .doOnNext(post -> validator.validate(post))
            .flatMap { chat: ChatDTO.Chat -> chatService.createChat(Chat.of(chat)) }
            .flatMap { chat: Chat ->
                ServerResponse
//                    .created(URI.create("/chats/" + chat.id))
                    .ok()
                    .build()
            }
    }

    fun findChatsByRoomId(request: ServerRequest) : Mono<ServerResponse> {
        val roomId = request.pathVariable("room-id")
        val chats : Flux<Chat> = chatService.findChatsByRoomId(ObjectId(roomId));
        return ServerResponse.ok().body(chats);
    }

//
//    fun updateBook(request: ServerRequest): Mono<ServerResponse> {
//        val bookId = request.pathVariable("book-id").toLong()
//        return request
//            .bodyToMono<BookDto.Patch>(BookDto.Patch::class.java)
//            .doOnNext { patch: BookDto.Patch? ->
////                validator.validate(
////                    patch
////                )
//            }
//            .flatMap<Book> { patch: BookDto.Patch ->
//                patch.bookId = bookId
//                bookService.updateBook(mapper.bookPatchToBook(patch))
//            }
//            .flatMap<ServerResponse> { book: Book? ->
//                ServerResponse.ok()
//                    .bodyValue(mapper.bookToResponse(book))
//            }
//    }
//
//    fun getBook(request: ServerRequest): Mono<ServerResponse> {
//        val bookId = request.pathVariable("book-id").toLong()
//
//        return bookService.findBook(bookId)
//            .flatMap { book: Book? ->
//                ServerResponse
//                    .ok()
//                    .bodyValue(mapper.bookToResponse(book))
//            }
//    }
//
//    fun getBooks(request: ServerRequest): Mono<ServerResponse> {
//        val pageAndSize = getPageAndSize(request)
//        return bookService.findBooks(pageAndSize.t1, pageAndSize.t2)
//            .flatMap { books: List<Book?>? ->
//                ServerResponse
//                    .ok()
//                    .bodyValue(mapper.booksToResponse(books))
//            }
//    }

    private fun getPageAndSize(request: ServerRequest): Tuple2<Long, Long> {
        val page = request.queryParam("page").map { s: String -> s.toLong() }.orElse(0L)
        val size = request.queryParam("size").map { s: String -> s.toLong() }.orElse(0L)
        return Tuples.of(page, size)
    }


}