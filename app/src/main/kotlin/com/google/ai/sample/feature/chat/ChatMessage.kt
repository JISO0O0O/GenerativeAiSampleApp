/* 채팅 메시지를 나타내는 데이터 클래스와 참여자 종류를 정의하는 열거형 클래스를 다룸
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ai.sample.feature.chat

//UUID를 이용해 메시지에 고유 식별자 부여
import java.util.UUID

//참여자 종류로 채팅 메시지의 발신자 또는 수신자를 나타냄
enum class Participant {
    USER, MODEL, ERROR
    /*순서대로
    * 사용자가 보낸 메시지, 모델(AI)가 보낸 메시지, 에러 상황에 대한 메시지
    * */
}

//채팅 메시지로 데이터 클래스는 개별 채팅 메시지의 정보를 담고 있음
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(), //고유 식별자
    var text: String = "",                        //메시지 내용
    val participant: Participant = Participant.USER, //메시지 참여자 종류
    var isPending: Boolean = false //메시지 처리 상채
)
