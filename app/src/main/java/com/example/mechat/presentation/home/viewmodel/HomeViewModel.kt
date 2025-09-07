package com.example.mechat.presentation.home.viewmodel

import ChatModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mechat.core.resources.AppException
import com.example.mechat.core.resources.DataResult
import com.example.mechat.domain.model.MessageModel

import com.example.mechat.domain.repository.ChatRepository
import com.example.smartjobreminder.core.resources.CurrentViewState
import com.example.smartjobreminder.core.resources.MessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val chatRepository : ChatRepository
) : ViewModel(){


    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()





    fun onAction(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.GetAllChatsList -> getAllChatsList()
            is HomeUiEvent.SelectedChat -> selectedChat(event.selectedChat)
            is HomeUiEvent.CreateMessage -> createMessage(event.onSuccess,event.onError,event.message)
            is HomeUiEvent.DeleteMultipleUserChat -> deleteMultipleUserChat(event.onSuccess,event.onError)
            is HomeUiEvent.DeleteMultipleUserMessage -> deleteMultipleUserMessage(event.onSuccess,event.onError)
            is HomeUiEvent.GetAllMessageList -> getAllMessageList(event.chat,event.onChatLoad)
            is HomeUiEvent.DeleteSelectedChat -> deleteSelectedChat(event.selectedChat)
            is HomeUiEvent.DeleteSelectedMessage -> deleteSelectedMessage(event.selectedMessage)
            is HomeUiEvent.InitAndChangeChatStatus -> initAndChangeChatStatus(event.onSuccess,event.onError,event.chat)
        }
    }

    private fun deleteSelectedMessage(selectedMessage: MessageModel) {
        _uiState.update { currentState ->
            val updatedList = if (currentState.deleteUserMessageList.contains(selectedMessage)) {
                // Remove the chat if it is already in the list
                currentState.deleteUserMessageList.toMutableList().apply {
                    remove(selectedMessage)
                }.toList()
            } else {
                // Add the chat if it's not in the list
                currentState.deleteUserMessageList.toMutableList().apply {
                    add(selectedMessage)
                }.toList()
            }
            currentState.copy(deleteUserMessageList = updatedList)
        }
    }

    private fun deleteSelectedChat(selectedChat: ChatModel) {
        _uiState.update { currentState ->
            val updatedList = if (currentState.deleteUserChatList.contains(selectedChat)) {
                // Remove the chat if it is already in the list
                currentState.deleteUserChatList.toMutableList().apply {
                    remove(selectedChat)
                }.toList()
            } else {
                // Add the chat if it's not in the list
                currentState.deleteUserChatList.toMutableList().apply {
                    add(selectedChat)
                }.toList()
            }
            currentState.copy(deleteUserChatList = updatedList)
        }
    }


    private fun getAllMessageList(chat: ChatModel, onChatLoad: () -> Unit) {
        _uiState.update {  it.copy(messageListState = CurrentViewState.LOADING ) }
        viewModelScope.launch(Dispatchers.IO){
            val dataResult : DataResult<Flow<List<MessageModel>>> = chatRepository.getAllMessageList(chat)
            when(dataResult) {
                is DataResult.Error<Flow<List<MessageModel>>> -> {
                    _uiState.update {
                        it.copy(error = dataResult.error, messageListState = CurrentViewState.ERROR )
                    }
                    onChatLoad()
                }
                is DataResult.Success<Flow<List<MessageModel>>> -> {
                    dataResult.data?.collect { userMessageList ->
                        _uiState.update {
                            it.copy(userMessageList = userMessageList ,messageListState = CurrentViewState.SUCCESS )
                        }
                    }
                }
            }
        }

    }

    private fun deleteMultipleUserMessage(
        onSuccess: (List<MessageModel>) -> Unit,
        onError: (AppException) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO){
            val dataResult : DataResult<List<MessageModel>> = chatRepository.deleteMultipleMessage(_uiState.value.deleteUserMessageList)
            when(dataResult) {
                is DataResult.Error<List<MessageModel>> -> {
                    onError(dataResult.error!!)
                }
                is DataResult.Success<List<MessageModel>> -> {
                    onSuccess(dataResult.data!!)

                }

            }
        }
    }

    private fun deleteMultipleUserChat(
        onSuccess: (List<ChatModel>) -> Unit,
        onError: (AppException) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO){
            val dataResult : DataResult<List<ChatModel>> = chatRepository.deleteMultipleChats(_uiState.value.deleteUserChatList)
            when(dataResult) {
                is DataResult.Error<List<ChatModel>> -> {
                    onError(dataResult.error!!)
                }
                is DataResult.Success<List<ChatModel>> -> {
                    onSuccess(dataResult.data!!)

                }

            }
        }
    }

    private fun createMessage(
        onSuccess: (MessageModel) -> Unit,
        onError: (AppException) -> Unit,
        message: MessageModel
    ) {

        viewModelScope.launch(Dispatchers.IO){
            val dataResult : DataResult<MessageModel> = chatRepository.createMessage(message)
            when(dataResult) {
                is DataResult.Error<MessageModel> -> {
                    onError(dataResult.error!!)
                }
                is DataResult.Success<MessageModel> -> {
                    onSuccess(dataResult.data!!)

                }

            }
        }

    }

    private fun selectedChat (chat : ChatModel){
        _uiState.update {
            it.copy(selectedChat =chat )
        }
    }





    private fun initAndChangeChatStatus(onSuccess : (ChatModel)-> Unit, onError : (AppException)->Unit, chat : ChatModel) {

        viewModelScope.launch(Dispatchers.IO){
            val dataResult : DataResult<ChatModel> = chatRepository.initAndChangeChatStatus(chat)
            when(dataResult) {
                is DataResult.Error<ChatModel> -> {
                    _uiState.value = _uiState.value.copy(error = dataResult.error, )
                    onError(dataResult.error!!)
                }
                is DataResult.Success<ChatModel> -> {
                    onSuccess(dataResult.data!!)
                }

            }
        }
    }






    private fun getAllChatsList() {
        _uiState.update {  it.copy(chatListState = CurrentViewState.LOADING ) }
        viewModelScope.launch(Dispatchers.IO){
            val dataResult : DataResult<Flow<List<ChatModel>>> = chatRepository.getAllUserChatList()
            when(dataResult) {
                is DataResult.Error<Flow<List<ChatModel>>> -> {
                    
                    _uiState.update {
                        it.copy(error = dataResult.error, chatListState = CurrentViewState.ERROR )
                    }
                }
                is DataResult.Success<Flow<List<ChatModel>>> -> {
                    dataResult.data?.collect { userChatList ->
                        val filteredChat = userChatList.filter { it.type != MessageType.NULL }
                        _uiState.update {
                            it.copy(userChatList = filteredChat ,chatListState = CurrentViewState.SUCCESS )
                        }
                    }
                }
            }
        }
    }





}