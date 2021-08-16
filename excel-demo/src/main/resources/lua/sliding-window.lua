local key = KEYS[1]
-- 一个时间窗口限制数量
local limit = tonumber(ARGV[1])
-- 获取当前时间 毫秒
local current_time = tonumber(ARGV[2])
-- 当前时间戳的key值， 唯一
local time_key = ARGV[3]
-- 获取时间窗口范围，单位秒，默认窗口1s
local time_range
if ARGV[4] == nil then
    time_range = 1000
else
    time_range = tonumber(ARGV[4]) * 1000
end
-- 获取集合key的过期时间， 当key过期时会存在瞬时并发的情况，因此过期时间不能太短或改用定时清除，单位秒，默认1小时
local expiration
if ARGV[5] == nil then
    expiration = 3600
else
    expiration = tonumber(ARGV[5])
end

local before_count = 0
local is_exists = redis.call("EXISTS", key)
if is_exists == 1 then
    -- 计算前一个时间窗口访问次数
    before_count = redis.call("ZCOUNT", key, current_time - time_range, current_time)
end
redis.call("ZADD", key, current_time, time_key)
local block = true
if (limit > before_count) then
    block = false
end

redis.call("EXPIRE", key, expiration)

return block
