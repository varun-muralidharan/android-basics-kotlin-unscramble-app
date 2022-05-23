/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.example.android.unscramble.ui.game.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.example.android.unscramble.ui.game.MAX_NO_OF_WORDS
import com.example.android.unscramble.ui.game.SCORE_INCREASE
import com.example.android.unscramble.ui.game.allWordsList
import com.example.android.unscramble.ui.game.model.GameViewModel

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding
    private val viewModel: GameViewModel by viewModels()

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = GameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup a click listener for the Submit and Skip buttons.
        binding.positive.setOnClickListener { viewModel.submitWord(binding.textInputEditText.text.toString()) }
        binding.negative.setOnClickListener { viewModel.skipWord() }
        // Update the UI
        setUpViewModel()
    }

    private fun setUpViewModel() {
        with(viewModel) {

            score.observe(viewLifecycleOwner) {
                binding.score.text = getString(R.string.score, it)
            }

            currentWordCount.observe(viewLifecycleOwner) {
                binding.wordCount.text = getString(R.string.word_count, it, MAX_NO_OF_WORDS)
                if (it >= MAX_NO_OF_WORDS) {
                    binding.positive.text = getString(R.string.restart)
                    binding.negative.text = getString(R.string.exit)

                    binding.positive.setOnClickListener { restartGame() }
                    binding.negative.setOnClickListener { exitGame() }
                } else {
                    binding.positive.text = getString(R.string.submit)
                    binding.negative.text = getString(R.string.skip)

                    binding.positive.setOnClickListener { viewModel.submitWord(binding.textInputEditText.text.toString()) }
                    binding.negative.setOnClickListener { viewModel.skipWord() }
                }
            }

            currentScrambledWord.observe(viewLifecycleOwner) {
                binding.textViewUnscrambledWord.text = it
            }

            incorrectWord.observe(viewLifecycleOwner) {
                setErrorTextField(it)
            }

        }
    }

    /*
     * Re-initializes the data in the ViewModel and updates the views with the new data, to
     * restart the game.
     */
    private fun restartGame() {
        setErrorTextField(false)
        viewModel.restartGame()
    }

    /*
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }

    /*
    * Sets and resets the text field error status.
    */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

}
