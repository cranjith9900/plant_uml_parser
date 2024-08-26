import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchGraphData } from './utils/flowSlice';
import { ReactFlowProvider } from 'reactflow';
import ReactFlow, { Background } from 'reactflow';
import 'reactflow/dist/style.css';

const FlowComponent = () => {
  const dispatch = useDispatch();
  const nodes = useSelector((state) => state.flow.nodes);
  const edges = useSelector((state) => state.flow.edges);
  const status = useSelector((state) => state.flow.status);
  const error = useSelector((state) => state.flow.error);
  const [plantUMLCode, setPlantUMLCode] = useState('');

  const handleChange = (e) => {
    setPlantUMLCode(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    dispatch(fetchGraphData(plantUMLCode));
  };

  return (
    <div>
      <h1>PlantUML to Graph</h1>
      <form onSubmit={handleSubmit}>
        <textarea
          value={plantUMLCode}
          onChange={handleChange}
          rows="10"
          cols="50"
          placeholder="Enter PlantUML code here"
        ></textarea>
        <br />
        <button type="submit">Generate Graph</button>
      </form>
      {status === 'loading' && <div>Loading...</div>}
      {status === 'failed' && <div>Error: {error}</div>}
      <div style={{ height: '100vh' }}>
        <ReactFlowProvider>
          <ReactFlow nodes={nodes} edges={edges}>
            <Background />
          </ReactFlow>
        </ReactFlowProvider>
      </div>
    </div>
  );
};

export default FlowComponent;
