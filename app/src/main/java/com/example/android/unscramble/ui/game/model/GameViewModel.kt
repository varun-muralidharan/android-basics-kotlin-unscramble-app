package com.example.android.unscramble.ui.game.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.android.unscramble.ui.game.SCORE_INCREASE
import com.example.android.unscramble.ui.game.allWordsList

class GameViewModel: ViewModel() {

    private var _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private var _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int> = _currentWordCount

    private var _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String> = _currentScrambledWord

    private var _incorrectWord = MutableLiveData<Boolean>()
    val incorrectWord: LiveData<Boolean> = _incorrectWord

    private var currentUnscrambledWord: String = ""

    init {
        currentUnscrambledWord = getNextWord()
        _currentScrambledWord.postValue(getNextScrambledWord(currentUnscrambledWord))
    }

    fun submitWord(word: String) {
        if (word == currentUnscrambledWord) {
            _score.postValue((_score.value ?: 0) + SCORE_INCREASE)
            _currentWordCount.postValue((_currentWordCount.value ?: 0) + 1)
            currentUnscrambledWord = getNextWord()
            _currentScrambledWord.postValue(getNextScrambledWord(currentUnscrambledWord))
            _incorrectWord.postValue(false)
        } else {
            _incorrectWord.postValue(true)
        }
    }

    fun skipWord() {
        _currentWordCount.postValue((_currentWordCount.value ?: 0) + 1)
        currentUnscrambledWord = getNextWord()
        _currentScrambledWord.postValue(getNextScrambledWord(currentUnscrambledWord))
        _incorrectWord.postValue(false)
    }

    private fun getNextScrambledWord(word: String): String {
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }

    private fun getNextWord(): String {
        return allWordsList.random()
    }

    fun restartGame() {
        _currentWordCount.postValue(0)
        _score.postValue(0)
        currentUnscrambledWord = getNextWord()
        _currentScrambledWord.postValue(getNextScrambledWord(currentUnscrambledWord))
    }

}