const fs = require('fs');

const input = fs.readFileSync('/tmp/aoc1', 'utf-8');
const sum = (xs) => xs.reduce((acc, x) =>  acc + x);
const product = (xs) => xs.reduce((acc, x) => acc * x);

const isSum2020 = (xs) => sum(xs) === 2020;
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

const tripleProduct = xs => {
  const out = [];
  xs.reduce((_, x, xIdx) => {
    xs.reduce((_, y, yIdx) => {
      xs.reduce((_, z, zIdx) => {
        if (
          xIdx !== yIdx &&
          xIdx !== zIdx &&
          yIdx !== zIdx
        ) {
          out.push([x, y, z]);
        };
      }, null);
    }, null);
  }, null);
  return out;
};

const part1 = input => {
  const xs = parseInput(input);
  const pairs = crossProduct(xs);
  const pair2020 = pairs.filter(isSum2020)[0];
  return product(pair2020);
};

const part2 = input => {
  const xs = parseInput(input);
  const triples = tripleProduct(xs);
  const triples2020 = triples.filter(isSum2020)[0];
  return product(triples2020);
};

module.exports = {
  isSum2020,
  parseInput,
  part1,
  crossProduct,
  tripleProduct,
  product,
  part2
};
