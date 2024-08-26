import React from "react";
import { Outlet } from "react-router-dom";
import { Provider } from "react-redux";
import appStore from "./utils/appStore";
import FlowComponent from "./fetchGraphData";
const App = () => {
  return (
    <div style={{ backgroundColor: "#fff", height: "100vh" }}>
      <Provider store={appStore}>
        <Outlet />
        <FlowComponent />
      </Provider>
    </div>
  );
};

export default App;
