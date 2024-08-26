import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { toast } from "react-toastify";
import { addEdge, applyNodeChanges, applyEdgeChanges } from "reactflow";
import axios from "axios";

// Define an async thunk to fetch graph data
export const fetchGraphData = createAsyncThunk('flow/fetchGraphData', async (plantUMLCode) => {
  const response = await axios.post('http://localhost:5000/generate-graph', plantUMLCode, {
    headers: {
      'Content-Type': 'text/plain',
    },
  });
  return response.data;
});

export const flow = createSlice({
  name: "flow",
  initialState: {
    nodes: [],
    edges: [],
    status: 'idle',
    error: null
  },
  reducers: {
    addNode: (state, action) => {
      let id;
      const existingIds = state.nodes.map((node) => node.id);

      for (let i = 1; ; i++) {
        id = `Node ${i}`;
        if (!existingIds.includes(id)) {
          break;
        }
      }
      const newNode = {
        id: id,
        type: "colorfulNode",
        data: id,
        position: {
          x: 400 + (Math.random() - 0.5) * 50,
          y: 300 + (Math.random() - 0.5) * 50,
        },
      };
      state.nodes.push(newNode);
      console.log("Nodes after adding:", state.nodes); // Log nodes after adding
      toast.success(`${id} created successfully!`);
    },

    updateNode: (state, action) => {
      const { id, data } = action.payload;
      state.nodes = state.nodes.map((node) => {
        if (node.id === id) {
          node.data = data;
        }
        return node;
      });
      console.log("Nodes after updating:", state.nodes); // Log nodes after updating
      toast.success(`Node updated successfully!`);
    },

    deleteNode: (state, action) => {
      const { id } = action.payload;
      state.nodes = state.nodes.filter((node) => node.id !== id);
      console.log("Nodes after deleting:", state.nodes); // Log nodes after deleting
      toast.success(`Node deleted successfully!`);
    },

    onNodesChange: (state, action) => {
      state.nodes = applyNodeChanges(action.payload, state.nodes);
      console.log("Nodes after applying changes:", state.nodes); // Log nodes after applying changes
    },

    onEdgesChange: (state, action) => {
      state.edges = applyEdgeChanges(action.payload, state.edges);
      console.log("Edges after applying changes:", state.edges); // Log edges after applying changes
      toast.success(`Edge changes applied successfully!`);
    },

    onConnect: (state, action) => {
      state.edges = addEdge({ ...action.payload, type: 'buttonedge' }, state.edges);
      console.log("Edges after connecting:", state.edges); // Log edges after connecting
      toast.success(`Edge created successfully!`);
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchGraphData.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchGraphData.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.nodes = action.payload.nodes;
        state.edges = action.payload.edges;
      })
      .addCase(fetchGraphData.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
        toast.error(`Failed to fetch graph data: ${action.error.message}`);
      });
  }
});

export const {
  addNode,
  updateNode,
  deleteNode,
  onNodesChange,
  onEdgesChange,
  onConnect,
} = flow.actions;

export default flow.reducer;
