  import { getBaseUrl, requestUtil } from "../../utils/requestUtil.js";
  import regeneratorRuntime from '../../lib/runtime/runtime';

  Page({
    data: {
      baseUrl: '',
      showPoster: false,
      posterImage: '',
      dateList: [],
      timeSlots: [],
      selectedDate: '',
      selectedTimeSlot: '',
      selectedVenueId: null,
      venues: [
        { id: 1, name: '1号场地', description: '暂无描述' },
        { id: 2, name: '2号场地', description: '暂无描述' },
        { id: 3, name: '3号场地', description: '暂无描述' },
        { id: 4, name: '4号场地', description: '用于对墙练习' }
      ],
      isBooking: false
    },
    onLoad: function (options) {
      const baseUrl = getBaseUrl();
      this.setData({
        baseUrl
      });
      this.getVenueData();
      this.getDateList();
    },
    async getVenueData() {
      try {
        const result = await requestUtil({
          url: "/venue/list",
          method: "GET"
        });
        if (result.code === 0) {
          this.setData({
            venues: result.data
          });
        }
      } catch (error) {
        console.error("获取场地数据失败", error);
      }
    },
    handleShare() {
      wx.showToast({
        title: '点击右上角分享',
        icon: 'none'
      });
    },
    async handleShowPoster() {
      try {
        wx.showLoading({
          title: '加载中...',
        });

        const result = await requestUtil({
          url: "/venue/getPoster",
          method: "GET"
        });

        if (result.code === 0) {
          // 预加载图片
          wx.getImageInfo({
            src: `${this.data.baseUrl}image/poster/${result.data.posterUrl}`,
            success: () => {
              this.setData({
                showPoster: true,
                posterImage: result.data.posterUrl
              });
            },
            fail: () => {
              wx.showToast({
                title: '图片加载失败',
                icon: 'none'
              });
            }
          });
        } else {
          wx.showToast({
            title: result.msg || '获取海报失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error("获取海报失败", error);
        wx.showToast({
          title: '获取海报失败',
          icon: 'none'
        });
      } finally {
        wx.hideLoading();
      }
    },
    handleClosePoster() {
      this.setData({
        showPoster: false
      });
    },
    async handleShowTicket() {
      try {
        const result = await requestUtil({
          url: "/venue/getReservation",
          method: "GET"
        });

        if (result.code === 0) {
          wx.previewImage({
            urls: [`${this.data.baseUrl}image/ticket/${result.data.ticketUrl}`],
            current: `${this.data.baseUrl}image/ticket/${result.data.ticketUrl}`
          });
        } else {
          wx.showToast({
            title: '获取凭证失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error("获取凭证失败", error);
        wx.showToast({
          title: '获取凭证失败',
          icon: 'none'
        });
      }
    },
    async handleBooking() {
      try {
        const { selectedDate, selectedVenueId } = this.data;
    
        // 检查是否选择了必要信息
        if (!selectedDate) {
          wx.showToast({
            title: '请选择预约日期',
            icon: 'none'
          });
          return;
        }
    
        if (!selectedVenueId) {
          wx.showToast({
            title: '请选择场地',
            icon: 'none'
          });
          return;
        }
    
        // 构造预约数据，作为 URL 参数传递
        const params = {
          venueId: selectedVenueId,
          date: selectedDate
        };
    
        console.log('发送预约请求：', params);
    
        // 显示加载提示
        wx.showLoading({
          title: '预约处理中',
          mask: true
        });
    
        // 手动构建带有查询参数的 URL
        const url = `/venue/reserve?${Object.keys(params).map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`).join('&')}`;
    
        // 发送预约请求，使用 URL 参数传递数据
        const result = await requestUtil({
          url: url,  // 使用带有查询参数的 URL
          method: "POST"
        });
    
        console.log('预约结果：', result);
    
        if (result.code === 0) {
          wx.showToast({
            title: '预约成功',
            icon: 'success',
            duration: 2000
          });
    
          // 清空已选信息
          this.setData({
            selectedDate: '',
            selectedVenueId: null
          });
        } else {
          wx.showToast({
            title: result.msg || '预约失败，请重试',
            icon: 'none',
            duration: 2000
          });
        }
      } catch (error) {
        console.error("预约失败", error);
        wx.showToast({
          title: '预约失败，请稍后重试',
          icon: 'none',
          duration: 2000
        });
      } finally {
        wx.hideLoading();
      }
    },
    async getDateList() {
      try {
        // 调用后端API获取可选日期列表
        const result = await requestUtil({
          url: "/venue/dates",  // API接口路径
          method: "GET"
        });

        if (result.code === 0) {
          // 格式化日期数据
          const formattedData = result.data.map(item => {
            if (typeof item.date === 'string') {
              // 移除时间部分和时区信息
              let formattedDate = item.date;
              if (formattedDate.indexOf('T') > -1) {
                formattedDate = formattedDate.split('T')[0];
              } else if (formattedDate.indexOf(' ') > -1) {
                formattedDate = formattedDate.split(' ')[0];
              }
              // 确保日期格式为YYYY-MM-DD
              const parts = formattedDate.split('-');
              if (parts.length === 3) {
                formattedDate = parts.join('-');
              }
              return { ...item, date: formattedDate };
            }
            return item;
          });

          // 设置日期列表数据
          this.setData({
            dateList: formattedData
          });
          
          // 如果有数据，默认选中第一个日期
          if (formattedData.length > 0) {
            this.setData({
              selectedDate: formattedData[0].date
            });
            // // 获取选中日期的时间段
            // this.getTimeSlots(formattedData[0].date);
          }
        } else {
          wx.showToast({
            title: '获取日期失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error("获取日期列表失败", error);
        wx.showToast({
          title: '获取日期失败',
          icon: 'none'
        });
      }
    },
    // async getTimeSlots(date) {
    //   try {
    //     const { selectedVenueId } = this.data;
        
    //     // 检查是否选择了场地
    //     if (!selectedVenueId) {
    //       wx.showToast({
    //         title: '请先选择场地',
    //         icon: 'none'
    //       });
    //       return;
    //     }

    //     // 确保日期格式为YYYY-MM-DD
    //     let formattedDate = date;
    //     if (typeof date === 'string') {
    //       formattedDate = date.split('T')[0].split(' ')[0];
    //     }
        
    //     console.log('请求时间段，日期为：', formattedDate, '场地ID：', selectedVenueId);
    //     const result = await requestUtil({
    //       url: `/venue/timeSlots?date=${formattedDate}&venueId=${selectedVenueId}`,
    //       method: "GET"
    //     });

    //     console.log('获取时间段结果：', result);
    //     if (result.code === 0) {
    //       // 格式化时间段数据
    //       const formattedTimeSlots = result.data.map(slot => ({
    //         ...slot,
    //         time: typeof slot === 'string' ? slot : slot.time
    //       }));

    //       this.setData({
    //         timeSlots: formattedTimeSlots,
    //         selectedTimeSlot: '' // 重置选中的时间段
    //       });
    //     } else {
    //       wx.showToast({
    //         title: result.msg || '获取时间段失败',
    //         icon: 'none'
    //       });
    //     }
    //   } catch (error) {
    //     console.error("获取时间段失败", error);
    //     wx.showToast({
    //       title: '获取时间段失败，请稍后重试',
    //       icon: 'none'
    //     });
    //   }
    // },

    handleDateSelect(e) {
      const index = e.currentTarget.dataset.index;
      const date = this.data.dateList[index].date;
      
      // 确保日期格式正确
      const formattedDate = date.split('T')[0].split(' ')[0];
      console.log('选择日期：', formattedDate);

      this.setData({
        selectedDate: formattedDate
      });

      this.getTimeSlots(formattedDate);
    },
    // handleTimeSlotSelect(e) {
    //   const index = e.currentTarget.dataset.index;
    //   const timeSlot = this.data.timeSlots[index];

    //   console.log('选择的时间段索引：', index);
    //   console.log('时间段数据：', timeSlot);

    //   if (!timeSlot) {
    //     wx.showToast({
    //       title: '无效的时间段',
    //       icon: 'none'
    //     });
    //     return;
    //   }

    //   // 检查时间段是否已满
    //   if (timeSlot.remaining !== undefined && timeSlot.remaining <= 0) {
    //     wx.showToast({
    //       title: '该时段已约满',
    //       icon: 'none'
    //     });
    //     return;
    //   }

    //   const selectedTime = typeof timeSlot === 'string' ? timeSlot : timeSlot.time;
    //   console.log('设置选中的时间段：', selectedTime);

    //   this.setData({
    //     selectedTimeSlot: selectedTime
    //   });
    // },
    handleVenueSelect(e) {
      const id = parseInt(e.currentTarget.dataset.id);
      console.log('选择场地ID：', id);
      
      this.setData({
        selectedVenueId: id,
        // timeSlots: [], // 清空之前的时间段
        // selectedTimeSlot: '' // 清空选中的时间段
      });

      // 如果已经选择了日期，则自动获取时间段
      // if (this.data.selectedDate) {
      //   this.getTimeSlots(this.data.selectedDate);
      // }
    },
    onShareAppMessage: function () {
      return {
        title: 'ZCMU滨文网球场reservation',
        path: '/pages/wenlv/index',
        imageUrl: '/icons/tennis.png' // 分享图片
      };
    },
    onShareTimeline: function () {
      return {
        title: 'ZCMU滨文网球场reservation',
        query: '',
        imageUrl: '/icons/tennis.png'
      };
    }
  });