const assert = require('assert');
const sut = require('../src/day1.js');

describe('isSum2020', () => {
  it('is true when sum of two numbers is 2020', () => {
    var input = [1010, 1010];
    assert.ok(sut.isSum2020(input));
  });
  it('is false when sum of two numbers is not 2020', () => {
    var input = [1, 1];
    assert.ok(!sut.isSum2020(input));
  });
});

describe('parseInput', () => {
  it('returns a list of integers', () => {
    var input = "123\n456\n789";
    assert.deepStrictEqual(sut.parseInput(input), [123, 456, 789]);
  });
});

describe('crossProduct', () => {
  it('constructs all combinations for an input array of numbers, without the diagonal', () => {
    var input = [1, 2, 3, 4];
    var expected = [
      [1, 2], [1, 3], [1, 4],
      [2, 1], [2, 3], [2, 4],
      [3, 1], [3, 2], [3, 4],
      [4, 1], [4, 2], [4, 3]
    ];
    assert.deepStrictEqual(sut.crossProduct(input), expected);
  });
});

describe('product', () => {
  it('computes the product of an array of numbers', () => {
    var input = [3, 4, 5];
    var expected = 3 * 4 * 5;
    assert.equal(sut.product(input), expected);
  });
});

describe('tripleProduct', () => {
  it('computes the combinations of triplets for an array, without the diagonal', () => {
    var input = [1, 2, 3, 4];
    var expected = [
      [1, 2, 3], [1, 2, 4],
      [1, 3, 2], [1, 3, 4],
      [1, 4, 2], [1, 4, 3],
      [2, 1, 3], [2, 1, 4],
      [2, 3, 1], [2, 3, 4],
      [2, 4, 1], [2, 4, 3],
      [3, 1, 2], [3, 1, 4],
      [3, 2, 1], [3, 2, 4],
      [3, 4, 1], [3, 4, 2],
      [4, 1, 2], [4, 1, 3],
      [4, 2, 1], [4, 2, 3],
      [4, 3, 1], [4, 3, 2]
    ];
    assert.deepStrictEqual(sut.tripleProduct(input), expected);
  });
});

describe('part1', () => {
  it('finds the pair of numbers that sums to 2020 and multiplies them', () => {
    var input = "1\n1009\n1011\n2\n3";
    var expected = 1009 * 1011;
    assert.equal(sut.part1(input), expected);
  });
});

describe('part2', () => {
  it('finds the triple of numbers that sums to 2020 and multiplies them', () => {
    var input = "2\n1008\n1009\n1011\n3";
    var expected = 3 * 1008 * 1009;
    assert.equal(sut.part2(input), expected);
  });
});
