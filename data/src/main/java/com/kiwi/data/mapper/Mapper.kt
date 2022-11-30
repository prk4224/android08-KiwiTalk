package com.kiwi.data.mapper

import com.kiwi.data.Const
import com.kiwi.data.mapper.DateFormatter.formatTimeString
import com.kiwi.data.model.remote.MarkerRemote
import com.kiwi.data.model.remote.NewChatRemote
import com.kiwi.data.model.remote.PlaceListRemote
import com.kiwi.data.model.remote.PlaceRemote
import com.kiwi.domain.model.*
import io.getstream.chat.android.client.models.Channel

object Mapper {
    fun MarkerRemote.toMarker() = Marker(
        cid = cid,
        x = x,
        y = y,
        keywords = keywords
    )

    fun PlaceListRemote.toPlaceList(): PlaceList {
        val result = mutableListOf<Place>()
        documents.mapIndexed { index, placeRemote ->
            result.add(index, placeRemote.toPlace())
        }
        return PlaceList(list = result)
    }

    fun PlaceRemote.toPlace() = Place(
        placeName = place_name,
        addressName = address_name,
        roadAddressName = road_address_name,
        lng = x,
        lat = y
    )

    /*
    * 키워드 경고뜨긴 하는데 데이터만 잘 넘어오면 제대로 작동함.
    * */
    fun Channel.toChatInfo() = ChatInfo(
        cid = this.cid,
        name = this.name,
        keywords = this.extraData[Const.MAP_KEY_KEYWORD] as? List<String>? ?: listOf("키워드가 없습니다"),
        description = "채팅방 설명이 없습니다.",
        memberCount = this.memberCount,
        lastMessageAt = this.lastMessageAt?.formatTimeString() ?: "오래전",
        address = this.extraData[Const.MAP_KEY_ADDRESS]?.toString() ?: Const.EMPTY_STRING
    )

    fun NewChat.toNewChatRemote() = NewChatRemote(
        imageUri = this.imageUri,
        chatName = this.chatName,
        chatDescription = this.chatDescription,
        maxPersonnel = this.maxPersonnel,
        keywords = this.keywords,
        address = this.address,
        lat = this.lat,
        lng = this.lng
    )
}