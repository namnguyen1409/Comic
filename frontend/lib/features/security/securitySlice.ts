// lib/features/security/securitySlice.ts

import { EndpointResponse } from "@/types/api.response";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";


export interface SecurityState {
    endpoints: EndpointResponse[];
    myRoles: string[];
    myPermissions: string[];
}

const initialSecurityState: SecurityState = {
    endpoints: [],
    myRoles: [],
    myPermissions: []
};

const securitySlice = createSlice({
    name: 'security',
    initialState: initialSecurityState,
    reducers: {
        setEndpoints: (state, action: PayloadAction<EndpointResponse[]>) => {
            state.endpoints = action.payload;
        },
        setMyRoles: (state, action: PayloadAction<string[]>) => {
            state.myRoles = action.payload;
        },
        setMyPermissions: (state, action: PayloadAction<string[]>) => {
            state.myPermissions = action.payload;
        }
    }
})

export const {
    setEndpoints,
    setMyRoles,
    setMyPermissions
} = securitySlice.actions;
export default securitySlice.reducer;