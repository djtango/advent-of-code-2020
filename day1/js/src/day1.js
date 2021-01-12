const fs = require('fs');

const input = fs.readFileSync('/tmp/aoc1', 'utf-8');

const isSum2020 = ([a, b]) => a + b === 2020;
const parseInput = inputString => inputString.split("\n").map((x) => parseInt(x));

const crossProduct = (xs) => {
  const out = [];
  xs.reduce((_, x, xIdx) => {
    xs.reduce((_, y, yIdx) => {
      if (xIdx !== yIdx) {
        out.push([x, y]);
      };
    }, null);
  }, null);
  return out;
};

const part1 = input => {
  const xs = parseInput(input);
  const pairs = crossProduct(xs);
  const pair2020 = pairs.filter(isSum2020)[0];
  return pair2020[0] * pair2020[1];
};

console.log(part1(input));

module.exports = {
  isSum2020,
  parseInput,
  part1,
  crossProduct
};
