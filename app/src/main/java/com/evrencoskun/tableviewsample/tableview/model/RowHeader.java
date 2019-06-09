/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.evrencoskun.tableviewsample.tableview.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class RowHeader extends Cell {
    public RowHeader(@NonNull String id, @Nullable String data) {
        super(id, data);
    }
}
