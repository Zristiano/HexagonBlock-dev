/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.example.fresco.postprocessor;

import com.facebook.imagepipeline.nativecode.NativeBlurFilter;
import com.facebook.imagepipeline.request.BasePostprocessor;

import android.graphics.Bitmap;

/**
 * Applies a blur filter using the
 * {@link NativeBlurFilter#iterativeBoxBlur(Bitmap, int, int)}, but does not
 * down-scale the image beforehand.
 *
 * 高斯模糊
 */
public class BlurPostprocessor extends BasePostprocessor
{
    /**
     * 默认取值范围，范围越大效果越明显
     */
    protected static final int BLUR_RADIUS = 25;

    /**
     * 重复次数，次数越多效果越明显
     */
    protected static final int BLUR_ITERATIONS = 3;

    protected int radius;

    protected int iterations;

    public BlurPostprocessor()
    {
        this(BLUR_RADIUS, BLUR_ITERATIONS);
    }

    public BlurPostprocessor(int radius, int iterations)
    {
        this.radius = radius <= 0 ? BLUR_RADIUS : radius;
        this.iterations = iterations <= 0 ? BLUR_ITERATIONS : iterations;
    }

    public void process(Bitmap bitmap)
    {
        NativeBlurFilter.iterativeBoxBlur(bitmap, radius, iterations);
    }
}
