package com.fourmob.colorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class ColorPickerPalette extends TableLayout {
	private String mDescription;
	private String mDescriptionSelected;
	private int mMarginSize;
	private int mNumColumns;
	public ColorPickerSwatch.OnColorSelectedListener mOnColorSelectedListener;
	private int mSwatchLength;

	public ColorPickerPalette(Context context) {
		super(context);
	}

	public ColorPickerPalette(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	private void addSwatchToRow(TableRow tableRow, View view, int line) {
		if (line % 2 == 0) {
			tableRow.addView(view);
			return;
		}
		tableRow.addView(view, 0);
	}

	private ImageView createBlankSpace() {
		ImageView imageView = new ImageView(getContext());
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(this.mSwatchLength, this.mSwatchLength);
		layoutParams.setMargins(this.mMarginSize, this.mMarginSize, this.mMarginSize, this.mMarginSize);
		imageView.setLayoutParams(layoutParams);
		return imageView;
	}

	private ColorPickerSwatch createColorSwatch(int color, int selectedColor) {
		ColorPickerSwatch colorPickerSwatch = new ColorPickerSwatch(getContext(), color, color == selectedColor, this.mOnColorSelectedListener);
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(this.mSwatchLength, this.mSwatchLength);
		layoutParams.setMargins(this.mMarginSize, this.mMarginSize, this.mMarginSize, this.mMarginSize);
		colorPickerSwatch.setLayoutParams(layoutParams);
		return colorPickerSwatch;
	}

	private TableRow createTableRow() {
		TableRow localTableRow = new TableRow(getContext());
		localTableRow.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
		return localTableRow;
	}

	private void setSwatchDescription(int rowNumber, int index, int rowElements, boolean selected,
									  View swatch) {
		int accessibilityIndex;
		if (rowNumber % 2 == 0) {
			// We're in a regular-ordered row
			accessibilityIndex = index;
		} else {
			// We're in a backwards-ordered row.
			int rowMax = ((rowNumber + 1) * mNumColumns);
			accessibilityIndex = rowMax - rowElements;
		}

		String description;
		if (selected) {
			description = String.format(mDescriptionSelected, accessibilityIndex);
		} else {
			description = String.format(mDescription, accessibilityIndex);
		}
		swatch.setContentDescription(description);
	}

	public void drawPalette(int[] colors, int selectedColor) {
		if (colors == null) {
			return;
		}

		this.removeAllViews();
		int tableElements = 0;
		int rowElements = 0;
		int rowNumber = 0;

		// Fills the table with swatches based on the array of colors.
		TableRow row = createTableRow();
		for (int color : colors) {
			tableElements++;

			View colorSwatch = createColorSwatch(color, selectedColor);
			setSwatchDescription(rowNumber, tableElements, rowElements, color == selectedColor,
					colorSwatch);
			addSwatchToRow(row, colorSwatch, rowNumber);

			rowElements++;
			if (rowElements == mNumColumns) {
				addView(row);
				row = createTableRow();
				rowElements = 0;
				rowNumber++;
			}
		}

		// Create blank views to fill the row if the last row has not been filled.
		if (rowElements > 0) {
			while (rowElements != mNumColumns) {
				addSwatchToRow(row, createBlankSpace(), rowNumber);
				rowElements++;
			}
			addView(row);
		}
	}

	public void init(int size, int numColumns, ColorPickerSwatch.OnColorSelectedListener onColorSelectedListener) {
		this.mNumColumns = numColumns;
		Resources resources = getResources();
		if (size == 1) {
			this.mSwatchLength = resources.getDimensionPixelSize(R.dimen.color_swatch_large);
			this.mMarginSize = resources.getDimensionPixelSize(R.dimen.color_swatch_margins_large);
		} else {
			this.mSwatchLength = resources.getDimensionPixelSize(R.dimen.color_swatch_small);
			this.mMarginSize = resources.getDimensionPixelSize(R.dimen.color_swatch_margins_small);
		}
		this.mOnColorSelectedListener = onColorSelectedListener;
		this.mDescription = resources.getString(R.string.color_swatch_description);
		this.mDescriptionSelected = resources.getString(R.string.color_swatch_description_selected);
	}
}