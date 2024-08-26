const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors'); 
const app = express();
const port = 5000;
app.use(cors());
// Middleware to parse plain text body
app.use(bodyParser.text({ type: 'text/plain' }));

// Helper function to parse PlantUML code and generate nodes and edges
function parsePlantUML(plantUMLCode) {
  const nodes = [];
  const edges = [];
  const nodeLabels = new Map();
  let yPos = 50;
  let xPos = 100;
  let lastNode = null;
  let branchStack = [];
  let branchCounter = 0;
  let repeatStack = [];

  // Split the PlantUML code into lines
  const lines = plantUMLCode.split('\n');
  lines.forEach((line, index) => {
    line = line.trim();

    // Handle start and stop
    if (line.startsWith('start') || line.startsWith('stop')) {
      const label = line;
      const nodeId = `Node_${index}`;
      nodeLabels.set(label, nodeId);
      nodes.push({
        id: nodeId,
        data: { label },
        position: { x: xPos, y: yPos }
      });
      yPos += 100;
      if (lastNode) {
        edges.push({
          id: `e${index}`,
          source: lastNode,
          target: nodeId,
          label: '',
          type: 'buttonedge'
        });
      }
      lastNode = nodeId;
    }

    // Handle node labels
    if (line.startsWith(':')) {
      const label = line.slice(1, -1).trim();
      const nodeId = `Node_${index}`;
      nodeLabels.set(label, nodeId);
      nodes.push({
        id: nodeId,
        data: { label },
        position: { x: xPos, y: yPos }
      });
      yPos += 100;
      if (lastNode) {
        edges.push({
          id: `e${index}`,
          source: lastNode,
          target: nodeId,
          label: '',
          type: 'buttonedge'
        });
      }
      lastNode = nodeId;
    }

    // Handle if conditions
    if (line.startsWith('if')) {
      const label = line.match(/\(([^)]+)\)/)[1];
      const nodeId = `Node_${index}_if`;
      nodes.push({
        id: nodeId,
        data: { label },
        position: { x: xPos, y: yPos }
      });
      yPos += 100;
      if (lastNode) {
        edges.push({
          id: `e${index}`,
          source: lastNode,
          target: nodeId,
          label: '',
          type: 'buttonedge'
        });
      }
      lastNode = nodeId;
      branchStack.push({ ifNode: nodeId, elseNode: null, branchCounter: ++branchCounter });
    }

    // Handle else conditions
    if (line.startsWith('else')) {
      const branch = branchStack[branchStack.length - 1];
      const nodeId = `Node_${index}_else`;
      nodes.push({
        id: nodeId,
        data: { label: 'else' },
        position: { x: xPos + 200, y: branchCounter * 100 - 50 }
      });
      branch.elseNode = nodeId;
      if (branch.ifNode) {
        edges.push({
          id: `e${index}_if_else`,
          source: branch.ifNode,
          target: nodeId,
          label: 'no',
          type: 'buttonedge'
        });
      }
      lastNode = nodeId;
    }

    // Handle endif conditions
    if (line.startsWith('endif')) {
      const branch = branchStack.pop();
      if (branch) {
        const nodeId = `Node_${index}_endif`;
        nodes.push({
          id: nodeId,
          data: { label: 'endif' },
          position: { x: xPos, y: yPos }
        });
        yPos += 100;
        if (lastNode) {
          edges.push({
            id: `e${index}_endif`,
            source: lastNode,
            target: nodeId,
            label: '',
            type: 'buttonedge'
          });
        }
        if (branch.ifNode) {
          edges.push({
            id: `e${index}_if_endif`,
            source: branch.ifNode,
            target: nodeId,
            label: 'yes',
            type: 'buttonedge'
          });
        }
        if (branch.elseNode) {
          edges.push({
            id: `e${index}_else_endif`,
            source: branch.elseNode,
            target: nodeId,
            label: '',
            type: 'buttonedge'
          });
        }
        lastNode = nodeId;
      }
    }

    // Handle repeat loops
    if (line.startsWith('repeat')) {
      const nodeId = `Node_${index}_repeat`;
      nodes.push({
        id: nodeId,
        data: { label: 'repeat' },
        position: { x: xPos, y: yPos }
      });
      yPos += 100;
      if (lastNode) {
        edges.push({
          id: `e${index}`,
          source: lastNode,
          target: nodeId,
          label: '',
          type: 'buttonedge'
        });
      }
      lastNode = nodeId;
      repeatStack.push(nodeId);
    }

    // Handle repeat while conditions
    if (line.startsWith('repeat while')) {
      const label = line.match(/\(([^)]+)\)/)[1];
      const nodeId = `Node_${index}_repeat_while`;
      nodes.push({
        id: nodeId,
        data: { label },
        position: { x: xPos, y: yPos }
      });
      yPos += 100;
      if (lastNode) {
        edges.push({
          id: `e${index}`,
          source: lastNode,
          target: nodeId,
          label: '',
          type: 'buttonedge'
        });
      }
      if (repeatStack.length > 0) {
        const repeatNodeId = repeatStack.pop();
        edges.push({
          id: `e${index}_repeat`,
          source: nodeId,
          target: repeatNodeId,
          label: 'yes',
          type: 'buttonedge'
        });
      }
      lastNode = nodeId;
    }
  });

  return { nodes, edges };
}

// Route to generate nodes and edges from PlantUML code
app.post('/generate-graph', (req, res) => {
  const plantUMLCode = req.body;

  if (!plantUMLCode) {
    return res.status(400).send('PlantUML code is required');
  }

  const graph = parsePlantUML(plantUMLCode);
  res.json(graph);
});

app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
