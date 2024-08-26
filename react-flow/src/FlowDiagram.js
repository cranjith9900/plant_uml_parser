import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import ReactFlow, { Background, Controls, MiniMap } from 'reactflow';
import 'reactflow/dist/style.css';
import { fetchGraphData, onNodesChange, onEdgesChange, onConnect } from './flowSlice';
import { toast } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

toast.configure();

const FlowDiagram = ({ plantUMLCode }) => {
  const dispatch = useDispatch();
  const { nodes, edges, status, error } = useSelector((state) => state.flow);

  useEffect(() => {
    if (plantUMLCode) {
      dispatch(fetchGraphData(plantUMLCode));
    }
  }, [plantUMLCode, dispatch]);

  return (
    <div style={{ height: '100vh' }}>
      {status === 'loading' && <p>Loading...</p>}
      {status === 'failed' && <p>Error: {error}</p>}
      {status === 'succeeded' && (
        <ReactFlow
          nodes={nodes}
          edges={edges}
          onNodesChange={(changes) => dispatch(onNodesChange(changes))}
          onEdgesChange={(changes) => dispatch(onEdgesChange(changes))}
          onConnect={(params) => dispatch(onConnect(params))}
          fitView
        >
          <MiniMap />
          <Controls />
          <Background />
        </ReactFlow>
      )}
    </div>
  );
};

export default FlowDiagram;
