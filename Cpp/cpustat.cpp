#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

struct cpu_core_stat
{
    // 用户态时间 nice用户态时间 内核态时间 空闲时间 I/O等待时间 硬中断时间 软中断时间 被盗时间 来宾时间 nice来宾时间
    uint64_t user, nice, system, idle, iowait, irq, softirq, steal, guest, guest_nice;
};

struct cpu_core_stat_pair
{
    struct cpu_core_stat stat[2]; // 0-start, 1-end
    long double occupy;
};

struct cpu_cores_occupy
{
    int nr_cpu_core;
    struct cpu_core_stat_pair *cpus_stat;
};

static int cpu_cores_occupy_init(struct cpu_cores_occupy *cco)
{
    if (!cco)
        return -1;
    cco->nr_cpu_core = sysconf(_SC_NPROCESSORS_ONLN); // CPU核心总数
    cco->cpus_stat = (struct cpu_core_stat_pair *)malloc(sizeof(struct cpu_core_stat_pair) * (cco->nr_cpu_core + 1));
    return 0;
}

static void cpu_cores_occupy_get(struct cpu_cores_occupy *cco, int stat_idx /*0-start, 1-end*/)
{
    FILE *fp;
    struct cpu_core_stat_pair *cpus_stat = cco->cpus_stat;

    fp = fopen("/proc/stat", "r");

    for (int i = 0; i <= cco->nr_cpu_core; i++)
    {
        fscanf(fp, "%*s %ld %ld %ld %ld %ld %ld %ld %ld %ld %ld",
               &cpus_stat[i].stat[stat_idx].user,
               &cpus_stat[i].stat[stat_idx].nice,
               &cpus_stat[i].stat[stat_idx].system,
               &cpus_stat[i].stat[stat_idx].idle,
               &cpus_stat[i].stat[stat_idx].iowait,
               &cpus_stat[i].stat[stat_idx].irq,
               &cpus_stat[i].stat[stat_idx].softirq,
               &cpus_stat[i].stat[stat_idx].steal,
               &cpus_stat[i].stat[stat_idx].guest,
               &cpus_stat[i].stat[stat_idx].guest_nice);
    }
    fclose(fp);
}

static void cpu_cores_occupy_call(struct cpu_cores_occupy *cco)
{
    struct cpu_core_stat_pair *cpus_stat = cco->cpus_stat;
    for (int i = 0; i <= cco->nr_cpu_core; i++)
    {
        struct cpu_core_stat *s0 = &cpus_stat[i].stat[0];
        struct cpu_core_stat *s1 = &cpus_stat[i].stat[1];
        long double s0_0 = (s0->user + s0->nice + s0->system);
        long double s1_0 = (s1->user + s1->nice + s1->system);
        long double s0_1 = (s0->user + s0->nice + s0->system + s0->idle);
        long double s1_1 = (s1->user + s1->nice + s1->system + s1->idle);
        cpus_stat[i].occupy = 100 * (s1_0 - s0_0) / (s1_1 - s0_1);
    }
}

static void cpu_cores_occupy_display(struct cpu_cores_occupy *cco)
{
    struct cpu_core_stat_pair *cpus_stat = cco->cpus_stat;
    system("clear");
    for (int i = 0; i <= cco->nr_cpu_core; i++)
    {
        if (i == 0)
        {
            printf("cpu \t%Lf %%\n", cpus_stat[i].occupy);
        }
        else
        {
            printf("cpu%d \t%Lf %%\n", i - 1, cpus_stat[i].occupy);
        }
    }
}

int main()
{
    struct cpu_cores_occupy cco;
    cpu_cores_occupy_init(&cco);

    for (;;)
    {
        cpu_cores_occupy_get(&cco, 0);
        sleep(1);
        cpu_cores_occupy_get(&cco, 1);
        cpu_cores_occupy_call(&cco);
        cpu_cores_occupy_display(&cco);
    }
}