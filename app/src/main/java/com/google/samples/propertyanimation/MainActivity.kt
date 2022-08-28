package com.google.samples.propertyanimation

import android.animation.*
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView


class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    // Extension Function for disabling the button when animation starts
    private fun ObjectAnimator.disableButtonWhenAnimationStart(view: View){
        addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }

    private fun rotater() {
        val animator = ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f)

        animator.duration = 1000

        // Prevent the user from clicking the button until the animation is done
        animator.disableButtonWhenAnimationStart(rotateButton)
        // Starting the animation is the last step after setting it all
        animator.start()
    }

    private fun translater() {
        val animator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f)
        // For Repeat
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE

        // Prevent the user from clicking the button until the animation is done
        animator.disableButtonWhenAnimationStart(translateButton)

        // Starting animation
        animator.start()

    }

    private fun scaler() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)

        // We can make setOfAnimations on object using ofPropertyValuesHolder
        val animator = ObjectAnimator.ofPropertyValuesHolder(star, scaleX, scaleY)

        // For Repeat
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE

        // Disabling the button when animation starts and enable it when animation ends
        animator.disableButtonWhenAnimationStart(scaleButton)

        // Starting the Animation
        animator.start()

    }

    private fun fader() {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)

        // For Repeat
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE

        // Disabling the button when animation starts and enable it when animation ends
        animator.disableButtonWhenAnimationStart(fadeButton)

        // Starting the Animation
        animator.start()

    }

    private fun colorizer() {
        val animator = ObjectAnimator.ofArgb(star.parent, "backgroundColor", Color.BLACK, Color.RED)

        animator.duration = 500
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableButtonWhenAnimationStart(colorizeButton)

        animator.start()
    }

    private fun shower() {
        // We want to create new star
        // Getting the star parent
        val container = star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height

        // Getting star dimensions
        var starW: Float = star.width.toFloat()
        var starH : Float = star.height.toFloat()

        // Getting the star
        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        container.addView(newStar)

        // When star is created change its size to have different sizes of stars
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        // Getting the newWidth and newHeight of star
        starH = newStar.scaleY
        starW = newStar.scaleX

        // Positioning the newStar horizontally in random position once it is created
        newStar.translationX = Math.random().toFloat() * containerW - starW/2

        // Now let's get the animations
        // The moving animation
        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y, -starH, containerH + starH)
        // Accelerate the star when it moves and falls down
        mover.interpolator = AccelerateInterpolator(1f)
        // The rotating animation
        val rotator = ObjectAnimator.ofFloat(newStar, View.ROTATION, (Math.random() * 1080).toFloat())
        rotator.interpolator = LinearInterpolator()

        // Getting all these animations in set of animation
        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 500).toLong()

        // Once the star falls down the bottom we should remove it from the container
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }
        })

        // Finally start the animation
        set.start()

    }

}
