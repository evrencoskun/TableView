/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.evrencoskun.tableview.pagination;

import androidx.annotation.Nullable;

public interface IPagination {

    /**
     * Loads the next page of the data set to the table view.
     */
    void nextPage();

    /**
     * Loads the previous page of the data set to the table view.
     */
    void previousPage();

    /**
     * Loads the data set of the specified page to the table view.
     *
     * @param page The page to be loaded.
     */
    void goToPage(int page);

    /**
     * Sets the number of items (rows) per page to be displayed in the table view.
     *
     * @param numItems The number of items per page.
     */
    void setItemsPerPage(int numItems);

    /**
     * Sets the OnTableViewPageTurnedListener for this Pagination.
     *
     * @param onTableViewPageTurnedListener The OnTableViewPageTurnedListener.
     */
    void setOnTableViewPageTurnedListener(@Nullable Pagination.OnTableViewPageTurnedListener onTableViewPageTurnedListener);

    /**
     * Removes the OnTableViewPageTurnedListener for this Pagination.
     */
    void removeOnTableViewPageTurnedListener();

    /**
     * @return The current page loaded to the table view.
     */
    int getCurrentPage();

    /**
     * @return The number of items per page loaded to the table view.
     */
    int getItemsPerPage();

    /**
     * @return The number of pages in the pagination.
     */
    int getPageCount();

    /**
     * @return Current pagination state of the table view.
     */
    boolean isPaginated();

}
