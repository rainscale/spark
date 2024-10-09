set nocompatible              " be iMproved, required
filetype off                  " required

" set the runtime path to include Vundle and initialize
set rtp+=~/.vim/bundle/Vundle.vim
call vundle#begin()
" alternatively, pass a path where Vundle should install plugins
"call vundle#begin('~/some/path/here')

" let Vundle manage Vundle, required
Plugin 'VundleVim/Vundle.vim'
" 文件目录管理器
Plugin 'scrooloose/nerdtree'
" 注释
Plugin 'scrooloose/nerdcommenter'
Plugin 'taglist.vim'
" 彩虹括号
Plugin 'luochen1990/rainbow'
" 代码自动格式化
Plugin 'vim-autoformat/vim-autoformat'
" 状态栏
Plugin 'vim-airline/vim-airline'
Plugin 'vim-airline/vim-airline-themes'
" Plugin 'ycm-core/YouCompleteMe'

" All of your Plugins must be added before the following line
call vundle#end()            " required
filetype plugin indent on    " required 自适应不同语言的智能缩进，插件开启
" To ignore plugin indent changes, instead use:
"filetype plugin on
"
" Brief help
" :PluginList       - lists configured plugins
" :PluginInstall    - installs plugins; append `!` to update or just :PluginUpdate
" :PluginSearch foo - searches for foo; append `!` to refresh local cache
" :PluginClean      - confirms removal of unused plugins; append `!` to auto-approve removal
"
" see :h vundle for more details or wiki for FAQ
" Put your non-Plugin stuff after this line

""""""""""""""""""""""BASE CONFIG"""""""""""""""""""""""
" 设置编码为 utf-8
set encoding=utf-8 fileencodings=ucs-bom,utf-8,cp936

" 解决 console 输出乱码
language messages zh_CN.utf-8

"Store lots of :cmdline history
set history=1000
        
" 禁止光标闪烁
set gcr=a:block-blinkon0

" 取消备份
set nobackup
set noswapfile

" 状态栏配置，总是显示状态栏
set laststatus=2
" set statusline=%F%r\ [HEX=%B][%l,%v,%P]\ %{strftime(\"%H:%M\")}

" 设在list模式
set list
set listchars=tab:>-,trail:-

" 显示光标当前位置
set ruler

" 在处理未保存或只读文件的时候，弹出确认 
set confirm

" 打开语法高亮
syntax enable

" 开启语法检测，允许用指定语法高亮配色方案替换默认方案
syntax on

" vimrc 文件修改之后自动加载
autocmd! bufwritepost .vimrc source %

" 文件修改之后自动载入
set autoread

" 高亮搜索命中的文本
set hlsearch

" 随着键入即时搜索
set incsearch

" 搜索时忽略大小写
set ignorecase

" 有一个或以上大写字母时仍大小写敏感
set smartcase

" 禁止环形搜索
" set nowrapscan

" 开启环形搜索(默认)
" set wrapscan

try
  colorscheme desert    " colorscheme molokai
catch
endtry

" 你可以在这里设置背景颜色为暗色模式
set background=dark
 
" 如果你想要在Vim启动时自动设置终端字体
if has('gui_running')
  " set guifont=Monaco:h14
  " set guifont=Menlo:h14
  " set guifont=Courier\ New:h10
  set guifont=Monospace\ 10
endif

" 如果你使用的是终端Vim，可以设置字体高度
" 注意：这只适用于终端Vim，不适用于GVim
if !has("gui_running")
    set termguicolors
    set t_Co=256
    let &guifont = "Monospace\ 10"
endif

" 在状态栏显示正在输入的命令
set showcmd

"Show current mode down the bottom
set showmode

" 显示括号配对情况
set showmatch

" :next, :make 命令之前自动保存
set autowrite

" 允许使用鼠标
set mouse=a

" 总在中间
:se so=999

" 设置行号
set number

" 退格键可用
set backspace=2

" 退格键一次删掉4个空格
set smarttab

" 缩进
set autoindent
set smartindent

" 保存文件时自动删除行尾空格或 Tab
autocmd BufWritePre * :%s/\s\+$//e

" 保存文件时自动删除末尾空行
autocmd BufWritePre * :%s/^$\n\+\%$//ge

" 保存文件时自动对中文中的英文单词和数字添加空格
" autocmd BufWritePre * silent! :%s/[^\x00-\xff]\zs\ze\w\|\w\zs\ze[^\x00-\xff]/ /g

" 将制表符扩展为空格
set expandtab
" 设置编辑时制表符占用空格数
set tabstop=4
" 设置格式化时制表符占用空格数
set shiftwidth=4
" 让vim把连续数量的空格视为一个制表符
set softtabstop=2
set shiftround

" 配置 go 文件 tab 显示方式：不填充 tab 但 tab 显示为 4 个空格的长度
autocmd BufNewFile,BufRead *.go setlocal noexpandtab tabstop=4 shiftwidth=4

" 代码折叠 用 za 命令折叠或展开
set foldmethod=indent
" 默认展开
set nofoldenable
" 有 6 种方法来选定折叠：
" 1    manual          手工定义折叠
" 2    indent           更多的缩进表示更高级别的折叠
" 3    expr              用表达式来定义折叠
" 4    syntax           用语法高亮来定义折叠
" 5    diff                对没有更改的文本进行折叠
" 6    marker           对文中的标志折叠


" 高亮突出显示当前行，列
set cursorline
set cursorcolumn

" 设置 退出 vim 后，内容显示在终端屏幕, 可以用于查看和复制
set t_ti= t_te=

" 打开文件时始终跳转到上次光标所在位置
autocmd BufReadPost *
      \ if ! exists("g:leave_my_cursor_position_alone") |
      \     if line("'\"") > 0 && line ("'\"") <= line("$") |
      \         exe "normal g'\"" |
      \     endif |
      \ endif

" 退出 vim 后，仍然可以 undo 上次编辑
if has('persistent_undo')      "check if your vim version supports it
  set undofile                 "turn on the feature
  set undodir=$HOME/.vim/undo  "directory where the undo files will be stored
endif

" 显示隐藏文件
let NERDTreeShowHidden=1
let NERDTreeWinPos=0

" NerdCommenter
let g:NERDSpaceDelims=1

set tags=./tags,./TAGS,tags;~,TAGS;~
set autochdir

" taglist设置
let Tlist_Use_Right_Window=1
let Tlist_Show_One_File=1
let Tlist_Exit_OnlyWindow=1
let Tlist_Auto_open=1

" cscope设置
" 告诉VIM可以使用quickfix窗口显示cscope的查找列表
set cscopequickfix=s-,c-,d-,i-,t-,e-
if has("cscope")
     set csprg=/usr/bin/cscope
     set csto=1  " 指定cstag的搜索顺序：0表示先搜索cscope数据库，若不匹配，再搜索tag文件，1则相反
     set cst    " :tag/Ctrl-]/vim -t将使用:cstag，而不是默认的:tag
     set nocsverb
     " add any database in current directory
     if filereadable("cscope.out")
         cs add cscope.out
     elseif $CSCOPE_DB != ""
         cs add $CSCOPE_DB
     endif
     set csverb
 endif

" 给cscope的关键命令添加快捷键支持
" symbol: find all references to the token under cursor
nmap <C-\>s :cs find s <C-R>=expand("<cword>")<CR><CR>
" global: find global definition(s) of the token under cursor
nmap <C-\>g :cs find g <C-R>=expand("<cword>")<CR><CR>
" calls: find all calls to the function name under cursor
nmap <C-\>c :cs find c <C-R>=expand("<cword>")<CR><CR>
" text: find all instances of the text under cursor
nmap <C-\>t :cs find t <C-R>=expand("<cword>")<CR><CR>
" egrep: egrep search for the word under cursor
nmap <C-\>e :cs find e <C-R>=expand("<cword>")<CR><CR>
" file: open the filename under cursor
nmap <C-\>f :cs find f <C-R>=expand("<cfile>")<CR><CR>
" includes: find files that include the filename under cursor
nmap <C-\>i :cs find i ^<C-R>=expand("<cfile>")<CR>$<CR>
" called: find functions that function under cursor calls
nmap <C-\>d :cs find d <C-R>=expand("<cword>")<CR><CR>

" 彩虹括号
let g:rainbow_active=1 "set to 0 if you want to enable it later via :RainbowToggle

" airline
" airline主题
if !exists('g:airline_theme')
    let g:airline_theme='random'
endif
" section c显示完整文件路径
let g:airline_section_c='%<%F%m %#__accent_red#%{airline#util#wrap(airline#parts#readonly(),0)}%#__restore__#'
" section y的时间格式
let g:airline_section_y='%{strftime("%H:%M")}'
" 开启 tabline
let g:airline#extensions#tabline#enabled=1
" tabline中当前buffer两端的分隔字符
let g:airline#extensions#tabline#left_sep=' '
" tabline中未激活buffer两端的分隔字符
let g:airline#extensions#tabline#left_alt_sep='|'
" tabline中buffer显示编号
let g:airline#extensions#tabline#buffer_nr_show=1
" tabline格式： default, jsformatter, unique_tail, unique_tail_improved
let g:airline#extensions#tabline#formatter='unique_tail_improved'
" https://github.com/vim-airline/vim-airline/blob/master/doc/airline.txt
let g:airline_powerline_fonts=1 " need nerdfont
let g:airline_detect_modified=1
let g:airline_detect_paste=1
let g:airline_detect_crypt=1
let g:airline_detect_spell=1
let g:airline_detect_spelllang=1
let g:airline_detect_iminsert=1
let g:airline_inactive_collapse=1
let g:airline_inactive_alt_sep=1
let g:airline_filetype_overrides = {
    \ 'coc-explorer':  [ 'CoC Explorer', '' ],
    \ 'defx':  ['defx', '%{b:defx.paths[0]}'],
    \ 'fugitive': ['fugitive', '%{airline#util#wrap(airline#extensions#branch#get_head(),80)}'],
    \ 'gundo': [ 'Gundo', '' ],
    \ 'help':  [ 'Help', '%f' ],
    \ 'minibufexpl': [ 'MiniBufExplorer', '' ],
    \ 'nerdtree': [ get(g:, 'NERDTreeStatusline', 'NERD'), '' ],
    \ 'startify': [ 'startify', '' ],
    \ 'vim-plug': [ 'Plugins', '' ],
    \ 'vimfiler': [ 'vimfiler', '%{vimfiler#get_status_string()}' ],
    \ 'vimshell': ['vimshell','%{vimshell#get_status_string()}'],
    \ 'vaffle' : [ 'Vaffle', '%{b:vaffle.dir}' ],
    \ }
let g:airline_exclude_preview=0
let g:airline#extensions#nerdtree_statusline=1

" AsyncRun
" automatically open quickfix window when AsyncRun command is executed
" set the quickfix window 6 lines height.
let g:asyncrun_open=6
" ring the bell to notify you job finished
let g:asyncrun_bell=1
" vim-airline displaying the status of AsyncRun
let g:asyncrun_status=''
let g:airline_section_error=airline#section#create_right(['%{g:asyncrun_status}'])

""""""""""""""""""""""按键映射""""""""""""""""""""""
" Map more <Leader>
let mapleader = " "
map \ <Leader>

" 系统剪贴板Copy
map <Leader>y "+y
" 系统剪贴板Paste
map <Leader>p "+p

" j k 移动行的时候光标始终在屏幕中间
nnoremap j jzz
nnoremap k kzz

" 映射切换 buffer的键位
nnoremap [b :bp<CR>
nnoremap ]b :bn<CR>

" 映射切换tab的键位
nnoremap [t :tabp<CR>
nnoremap ]t :tabn<CR>

" normal 模式下 Ctrl+c 全选并复制到系统剪贴板(linux 必须装有 vim-gnome)
nmap <C-c> gg"+yG

" visual 模式下 Ctrl+c 复制选中内容到剪贴板
vmap <C-c> "+y

" Ctrl+v 原样粘贴剪切板内容
inoremap <C-v> <ESC>"+pa

" w!!写入只读文件
cmap w!! w !sudo tee >/dev/null %

" 在 Normal Mode 和 Visual/Select Mode 下，利用 > 键和 < 键来缩进文本
nnoremap > >>
nnoremap < <<
vnoremap > >gv
vnoremap < <gv

" quicker window switching
nnoremap <C-h> <C-w>h
nnoremap <C-j> <C-w>j
nnoremap <C-k> <C-w>k
nnoremap <C-l> <C-w>l

" quicker window resize
nnoremap <C-S-Left> <C-w><
nnoremap <C-S-Right> <C-w>>
nnoremap <C-S-Up> <C-w>+
nnoremap <C-S-Down> <C-w>-

" 关闭当前分割窗口
nnoremap <leader>q :q<cr>

" 保存所有窗口内容并退出vim
nnoremap <leader>WQ :wa<cr>:q<cr>

" 不做任何保存，直接退出vim
nnoremap <leader>Q :qa!<cr>

" F2 切换行号显示
nnoremap <F2> :set nonu!<CR>:set foldcolumn=0<CR>

" F3 打开目录树
map <f3> :NERDTreeToggle<cr>

" <F6> 新建标签页
map <F6> <Esc>:tabnew<CR>

" <F9> 代码自动格式化
noremap <F9> :Autoformat<cr>:w<cr><cr>
