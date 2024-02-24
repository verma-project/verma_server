import LoginView from 'Frontend/views/login/LoginView.js';
import { createBrowserRouter, RouteObject } from 'react-router-dom';
import {protectRoutes} from "@hilla/react-auth";

// @ts-ignore
export const routes = protectRoutes([{ path: '/login', element: <LoginView /> }]) as RouteObject[];

export default createBrowserRouter(routes);
